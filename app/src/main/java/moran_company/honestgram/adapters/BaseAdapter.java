package moran_company.honestgram.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Kulykov Anton on 9/8/17.
 */

public abstract class BaseAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH>
        implements BaseViewHolder.OnViewHolderEventListener {

    protected Context mContext;

    protected List<T> items;

    protected OnItemClickListener<T> onItemClickListener;

    public BaseAdapter() {
    }

    public BaseAdapter(Context context) {
        this.mContext = context;
    }

    public BaseAdapter(List<T> items) {
        this.items = items;
    }

    public BaseAdapter(Context context, List<T> items) {
        this.items = items;
        this.mContext = context;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        if (this.items == null) setItems(items);
        else {
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (items != null) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    public boolean updateItem(int position, T newItem) {
        if (position < 0 || position >= getItemCount()) return false;
        items.set(position, newItem);
        notifyItemChanged(position);
        return true;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressWarnings("unchecked")
        Class<VH> vhClass = (Class<VH>) getParameterType(1, RecyclerView.ViewHolder.class);
        try {
            Constructor<VH> constructor = vhClass.getDeclaredConstructor(this.getClass(),
                    BaseViewHolder.OnViewHolderEventListener.class, View.class);
            constructor.setAccessible(true);
            return constructor.newInstance(this, this, LayoutInflater.from(parent.getContext())
                    .inflate(getItemLayout(viewType), parent, false));
        } catch (Exception e) {
            try {
                Constructor<VH> constructor = vhClass.getDeclaredConstructor(
                        BaseViewHolder.OnViewHolderEventListener.class, View.class);
                constructor.setAccessible(true);
                return constructor.newInstance(this, LayoutInflater.from(parent.getContext())
                        .inflate(getItemLayout(viewType), parent, false));
            } catch (Exception e1) {
                e.printStackTrace();
                e1.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    protected abstract int getItemLayout(int viewType);

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Nullable
    public T getItem(int position) {
        return position < 0 || position >= getItemCount() ? null : items.get(position);
    }

    @Override
    public void onViewHolderClick(View itemView, int position) {
        if (onItemClickListener == null) return;
        T item = getItem(position);
        if (item != null) onItemClickListener.onItemClick(itemView, item);
    }

    private Type getParameterType(int index, Type baseType) {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (index < types.length) return types[index];
        }
        return baseType;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View itemView, T item);
    }

    public void setContext(Context context) {
        mContext = context;
    }
}
