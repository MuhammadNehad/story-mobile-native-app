package mainproject.mainroject.story;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class viewHolderImpl extends RecyclerView.ViewHolder {

    View view;
    public viewHolderImpl(@NonNull View itemView) {
        super(itemView);
        this.view =itemView;
    }
    public View getViewHolder()
    {
        return view;
    }

}
