package com.a21vianet.wallet.vport;

import android.support.test.runner.AndroidJUnit4;

import com.a21vianet.wallet.vport.http.Api;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.IPFSRequest;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.AddResult;
import com.google.gson.Gson;
import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.http.ErrorResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void testIpfsAdd() {
        String toJson = new Gson().toJson(new User("张三", "0X008975766666666", 1));
        new IPFSRequest(Api.IPFSApi).ipfsAddJson(new BaseHttpSubscriber<AddResult>() {
            @Override
            protected void onError(ErrorResponse error) {
                super.onError(error);
            }

            @Override
            protected void onSuccess(AddResult addResult) {
                assertEquals(addResult.getHash(), "QmQD9BZqnabKm7ErxjTPCgV1CxWboQCBkpQFdQZy5h1GgK");
            }
        }, toJson);
    }

    @Test
    public void testIpfsGet() {
        final String toJson = new Gson().toJson(new User("张三", "0X008975766666666", 1));
        new IPFSRequest(Api.IPFSApi).ipfsGetJson(new BaseHttpSubscriber<String>() {
            @Override
            protected void onError(ErrorResponse error) {
                super.onError(error);
            }

            @Override
            protected void onSuccess(String s) {
                assertEquals(s, toJson);
            }
        }, "QmQD9BZqnabKm7ErxjTPCgV1CxWboQCBkpQFdQZy5h1GgK");
    }
}
