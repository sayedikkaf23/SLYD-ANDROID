package io.isometrik.groupstreaming.ui.utils;

public interface BasePresenter<T> {
  void attachView(T view);

  void detachView();
}
