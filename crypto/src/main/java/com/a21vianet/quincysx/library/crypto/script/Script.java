package com.a21vianet.quincysx.library.crypto.script;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import static com.a21vianet.quincysx.library.crypto.script.ScriptOpCodes.OP_0;
import static com.a21vianet.quincysx.library.crypto.script.ScriptOpCodes.OP_1;
import static com.a21vianet.quincysx.library.crypto.script.ScriptOpCodes.OP_1NEGATE;

public class Script {
    private LinkedList<ScriptChunk> mScriptChunks;

    public static final long MAX_SCRIPT_ELEMENT_SIZE = 520;  // bytes

    public Script(LinkedList<ScriptChunk> scriptChunk) {
        mScriptChunks = scriptChunk;
    }

    public byte[] getProgram() {
        try {
            byte[] program = null;
            // Don't round-trip as Satoshi's code doesn't and it would introduce a mismatch.
            if (program != null)
                return Arrays.copyOf(program, program.length);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (ScriptChunk chunk : mScriptChunks) {
                chunk.write(bos);
            }
            program = bos.toByteArray();
            return program;
        } catch (IOException e) {
            throw new RuntimeException(e);  // Cannot happen.
        }
    }

    public static int encodeToOpN(int value) {
        if (value == 0)
            return OP_0;
        else if (value == -1)
            return OP_1NEGATE;
        else
            return value - 1 + OP_1;
    }

    @Override
    public String toString() {
        return mScriptChunks.toString();
    }

}
