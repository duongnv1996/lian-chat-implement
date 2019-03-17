package com.skynet.lian.ui.chatgroup.editgroup;

import com.skynet.lian.models.Room;
import com.skynet.lian.ui.base.Presenter;


public class EditPresenter extends Presenter<EditContract.View> implements EditContract.Presenter {
    EditContract.Interactor interactor;

    public EditPresenter(EditContract.View view) {
        super(view);
        interactor = new EditImplRemote(this);
    }




    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onErrorApi(String message) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onErrorApi(message);
        }
    }

    @Override
    public void onError(String message) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onError(message);
        }
    }

    @Override
    public void onErrorAuthorization() {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onErrorAuthorization();
        }
    }

    @Override
    public void edtChatGroup(String name, String idGroup) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.edtChatGroup(name, idGroup);
        }
    }

    @Override
    public void deleteChat(String id) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.deleteChat(id);
        }
    }

    @Override
    public void leaveGroupChat(String id) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.leaveGroupChat(id);
        }
    }

    @Override
    public void addUserToGroup(String idGroup, String idUser) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.addUserToGroup(idGroup,idUser);
        }
    }

    @Override
    public void onSucessEdtChatGroup() {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onSucessEdtChatGroup();
        }
    }

    @Override
    public void onSucessDeleteChat() {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onSucessDeleteChat();
        }
    }

    @Override
    public void onSucessLeaveGroupChat() {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onSucessLeaveGroupChat();
        }
    }

    @Override
    public void onSucessAddUserToGroup(Room room) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onSucessAddUserToGroup(room);
        }
    }
}
