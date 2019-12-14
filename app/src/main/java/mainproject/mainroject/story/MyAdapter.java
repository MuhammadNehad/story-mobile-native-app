package mainproject.mainroject.story;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter<cLass> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;
    ILoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<cLass> items;
    int visibleThrehold=5;
    int lastVisibleItem,totalItemCount;

    public MyAdapter(RecyclerView recyclerView,Activity activity,List<cLass> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount=linearLayoutManager.getItemCount();
                lastVisibleItem=linearLayoutManager.findLastVisibleItemPosition();
                if(!isLoading&&totalItemCount<=(lastVisibleItem+visibleThrehold))
                {
                    if(loadMore != null)
                    {
                        loadMore.onLoadMore();
                    }

                }
                isLoading = true;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position)==null?VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM)
        {
            View view = LayoutInflater.from(activity).inflate(R.layout.fragment_followings,parent,false);
            return new viewHolderImpl(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
