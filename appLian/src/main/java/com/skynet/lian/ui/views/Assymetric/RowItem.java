package com.skynet.lian.ui.views.Assymetric;

final class RowItem {
  private final AsymmetricItem item;
  private final int index;

  RowItem(int index, AsymmetricItem item) {
    this.item = item;
    this.index = index;
  }

  AsymmetricItem getItem() {
    return item;
  }

  int getIndex() {
    return index;
  }
}
