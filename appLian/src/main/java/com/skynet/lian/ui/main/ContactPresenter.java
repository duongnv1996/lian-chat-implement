package com.skynet.lian.ui.main;

import android.content.ContentResolver;

import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.Presenter;

import java.util.List;

public class ContactPresenter extends Presenter<ContactContract.View> implements ContactContract.Presenter {
    ContactContract.Interactor interactor;

    public ContactPresenter(ContactContract.View view) {
        super(view);
        interactor = new ContactImplRemote(this);
    }


    @Override
    public void getListBussinessContact() {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getListBussinessContact();
        }
    }

    @Override
    public void getListContact(ContentResolver cr) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getListContact(cr);
        }
    }

    @Override
    public void updateToken() {
        interactor.updateToken();
    }

    @Override
    public void acceptFriend(String id) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.acceptFriend(id);
        }
    }

    @Override
    public void getListFriend() {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getListFriend();
        }
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
    public void onSucessAddFriend() {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onSucessAddFriend();
        }
    }

    @Override
    public void onSucessListContact(List<Profile> list) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            if(list != null &&!list.isEmpty())
            view.onSucessListContact(list);
        }
    }
}
