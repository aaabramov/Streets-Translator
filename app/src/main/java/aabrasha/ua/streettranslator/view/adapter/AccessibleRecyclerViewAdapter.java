package aabrasha.ua.streettranslator.view.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.Collection;

/**
 * @author Andrii Abramov on 9/4/16.
 */
public abstract class AccessibleRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public abstract T getItem(int position);

    public abstract void setItems(Collection<T> items);

    public abstract void remove(int position);
    public abstract void remove(T item);
}
