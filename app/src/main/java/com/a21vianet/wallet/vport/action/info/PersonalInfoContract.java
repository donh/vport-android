package com.a21vianet.wallet.vport.action.info;


import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TResult;
import com.littlesparkle.growler.core.ui.mvp.BaseView;

public interface PersonalInfoContract {

    interface View extends BaseView {
        void initHeadPopWindow();

        void setViewData();

        TakePhoto getTakePhoto();

        void showHardImage(String image);
    }

    interface Presenter {

        void getPhotoByCamera();

        void getPhotoByAlbums();

        void onTakeSuccess(TResult tResult);
    }

}
