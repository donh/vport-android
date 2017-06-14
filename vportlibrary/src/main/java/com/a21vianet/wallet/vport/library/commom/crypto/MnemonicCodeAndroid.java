/*
 *
 *  * Copyright 2014 http://Bither.net
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.a21vianet.wallet.vport.library.commom.crypto;


import com.a21vianet.quincysx.library.crypto.crypto.mnemonic.MnemonicCode;
import com.a21vianet.quincysx.library.crypto.crypto.mnemonic.MnemonicWordList;
import com.a21vianet.wallet.app.R;
import com.a21vianet.wallet.app.WalletApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by songchenwen on 14/12/31.
 */
public class MnemonicCodeAndroid extends MnemonicCode {

    public MnemonicCodeAndroid() throws IOException {
        super();
    }

    static public void setMnemonicCode(MnemonicWordList mnemonicWordList) throws IOException {
        MnemonicCode mnemonicCode = new MnemonicCodeAndroid();
        mnemonicCode.setMnemonicWordList(mnemonicWordList);
        MnemonicCode.setInstance(mnemonicCode);
    }

    @Override
    protected HashMap<MnemonicWordList, InputStream> openWordList() throws IOException,
            IllegalArgumentException {
        return getAllMnemonicWordListRawResources();
    }

    private HashMap<MnemonicWordList, InputStream> getAllMnemonicWordListRawResources() {
        ArrayList<MnemonicWordList> mnemonicWordLists = MnemonicWordList.getAllMnemonicWordLists();
        HashMap<MnemonicWordList, InputStream> inputStreamMap = new HashMap<>();
        for (MnemonicWordList mnemonicWordList : mnemonicWordLists) {
            inputStreamMap.put(mnemonicWordList, getMnemonicWordListRawResource(mnemonicWordList));
        }
        return inputStreamMap;
    }

    private InputStream getMnemonicWordListRawResource(MnemonicWordList wordList) {
        switch (wordList) {
            case English:
                return WalletApplication.getContext().getResources().openRawResource(R.raw
                        .mnemonic_wordlist_english);
            case ZhCN:
                return WalletApplication.getContext().getResources().openRawResource(R.raw
                        .mnemonic_wordlist_zh_cn);
            case ZhTw:
                return WalletApplication.getContext().getResources().openRawResource(R.raw
                        .mnemonic_wordlist_zh_tw);
        }
        return WalletApplication.getContext().getResources().openRawResource(R.raw
                .mnemonic_wordlist_english);
    }

}
