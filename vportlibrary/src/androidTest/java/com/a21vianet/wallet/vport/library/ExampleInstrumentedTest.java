package com.a21vianet.wallet.vport.library;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
//        final Context appContext = InstrumentationRegistry.getTargetContext();
//
//        CryptoManager.getInstance().generateBitcoinKeyPair("1234", new GenerateCallBack() {
//            @Override
//            public void onSuccess() {
//
//                test(appContext);
//
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });

    }

    private void test(final Context context) {
//        try {
//            BitcoinKey coinKey = CryptoManager.getInstance().getCoinKey();
//            Log.e("=======priv", coinKey.getPrivKey());
//            Log.e("=======pub", coinKey.getPubKey());
//            Log.e("=======rawpriv", coinKey.getRawPrivKey());
//            Log.e("=======rawpub", coinKey.getRawPubKey());
//            Log.e("=======addr", CryptoManager.getInstance().generateBitcoinAddress());
//
//            CryptoManager.getInstance().signJWTToken(context, "{\"issuedAt\": \"1440713414.19\"}",
//                    new OnFinishedListener() {
//                        @Override
//                        public void onFinished(String s) {
//                            Log.e("=====signJWTToken=== ", s);
//
//                            CryptoManager.getInstance().decodeJWTToken(context, s, new
//                                    OnFinishedListener() {
//                                        @Override
//                                        public void onFinished(String s) {
//                                            Log.e("=====decodeJWTToken=== ", s);
//
//                                            try {
//                                                CryptoManager.getInstance().verifyJWTToken
//                                                        (context, s, new OnVerifiedListener() {
//                                                            @Override
//                                                            public void onVerified(boolean isValid) {
//                                                                Log.e("=====verifyJWTToken=== ",
//                                                                        isValid
//                                                                        + "");
//                                                                assertEquals(isValid, true);
//                                                            }
//                                                        });
//                                            } catch (NoDecryptException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    });
//                        }
//                    });
//        } catch (NoDecryptException e) {
//            e.printStackTrace();
//        }

    }
}
