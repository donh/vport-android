/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.a21vianet.quincysx.library.crypto.script;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.a21vianet.quincysx.library.crypto.script.ScriptOpCodes.OP_0;
import static com.a21vianet.quincysx.library.crypto.script.ScriptOpCodes.OP_CHECKMULTISIG;
import static com.a21vianet.quincysx.library.crypto.script.ScriptOpCodes.OP_PUSHDATA1;
import static com.a21vianet.quincysx.library.crypto.script.ScriptOpCodes.OP_PUSHDATA2;
import static com.a21vianet.quincysx.library.crypto.script.ScriptOpCodes.OP_PUSHDATA4;
import static com.a21vianet.quincysx.library.crypto.guava.Preconditions.checkArgument;

/**
 * <p>Tools for the construction of commonly used script types. You don't normally need this as
 * it's hidden behind
 * with the
 * protocol at a lower level.</p>
 */
public class ScriptBuilder {
    private LinkedList<ScriptChunk> chunks;

    public ScriptBuilder() {
        chunks = new LinkedList();
    }

    public ScriptBuilder addChunk(ScriptChunk chunk) {
        chunks.add(chunk);
        return this;
    }

    public ScriptBuilder op(int opcode) {
        checkArgument(opcode > OP_PUSHDATA4);
        return addChunk(new ScriptChunk(opcode, null));
    }

    public ScriptBuilder data(byte[] data) {
        // implements BIP62
        byte[] copy = Arrays.copyOf(data, data.length);
        int opcode;
        if (data.length == 0) {
            opcode = OP_0;
        } else if (data.length == 1) {
            byte b = data[0];
            if (b >= 1 && b <= 16)
                opcode = Script.encodeToOpN(b);
            else
                opcode = 1;
        } else if (data.length < OP_PUSHDATA1) {
            opcode = data.length;
        } else if (data.length < 256) {
            opcode = OP_PUSHDATA1;
        } else if (data.length < 65536) {
            opcode = OP_PUSHDATA2;
        } else {
            throw new RuntimeException("Unimplemented");
        }
        return addChunk(new ScriptChunk(opcode, copy));
    }

    public ScriptBuilder smallNum(int num) {
        checkArgument(num >= 0, "Cannot encode negative numbers with smallNum");
        checkArgument(num <= 16, "Cannot encode numbers larger than 16 with smallNum");
        return addChunk(new ScriptChunk(Script.encodeToOpN(num), null));
    }

    public Script build() {
        return new Script(chunks);
    }

    /**
     * Creates a program that requires at least N of the given keys to sign, using OP_CHECKMULTISIG.
     */
    public static Script createMultiSigOutputScript(int threshold, List<byte[]> pubkeys) {
        checkArgument(threshold > 0);
        checkArgument(threshold <= pubkeys.size());
        checkArgument(pubkeys.size() <= 16);  // That's the max we can represent with a single
        // opcode.
        ScriptBuilder builder = new ScriptBuilder();
        builder.smallNum(threshold);
        for (byte[] pubs : pubkeys) {
            builder.data(pubs);
        }
        builder.smallNum(pubkeys.size());
        builder.op(OP_CHECKMULTISIG);
        return builder.build();
    }
}
