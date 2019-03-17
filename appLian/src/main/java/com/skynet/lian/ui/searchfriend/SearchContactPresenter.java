package com.skynet.lian.ui.searchfriend;

import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.Presenter;

import java.util.List;

public class SearchContactPresenter extends Presenter<SearchContactContract.View> implements SearchContactContract.Presenter {
    SearchContactContract.Interactor interactor;

    public SearchContactPresenter(SearchContactContract.View view) {
        super(view);
        interactor = new SearchContactImplRemote(this);
    }



    @Override
    public void getListContact(String cr) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getListContact(cr);
        }
    }

    @Override
    public void addFriend(String idFriend) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.addFriend(idFriend);
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
