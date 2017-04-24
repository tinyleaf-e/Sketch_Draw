package com.example.zhangtao103239.sketchdraw;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 晔 on 2017/4/14.
 */

public class mypaintingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mypainting_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.myPainting_return_button)
    public void onmyPainting_return_buttonClick()
    {
        getFragmentManager().popBackStack();
    }

//    public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ImageHolder> {
//
//        private List<String> dataList;
//        private Context context;
//        private LayoutInflater inflater;
//        private OnRecyclerItemClickListener itemClickListener;
//
//        //自定义监听事件
//        public static interface OnRecyclerItemClickListener {
//            void onItemClick(View view);
//            void onItemLongClick(View view);
//        }
//        private OnRecyclerItemClickListener mOnItemClickListener = null;
//        public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
//            mOnItemClickListener = listener;
//        }
//
//        public ImageRecyclerAdapter(Context context, List<String> dataList) {
//            this.context = context;
//            this.dataList = dataList;
//            inflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getItemCount() {
//            return dataList.size();
//        }
//
//        @Override
//        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ImageHolder(inflater.inflate(R.layout.item_image, parent, false), itemClickListener);
//        }
//
//        @Override
//        public void onBindViewHolder(ImageHolder holder, int position) {
//            Picasso.with(context).load(dataList.get(position)).resize(200, 200).centerCrop().into(holder.image);
//        }
//
//        public void setItemClickListener(OnRecyclerItemClickListener itemClickListener) {
//            this.itemClickListener = itemClickListener;
//        }
//
//        public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//            private ImageView image;
//            private OnRecyclerItemClickListener itemClickListener;
//
//            public ImageHolder(View itemView, OnRecyclerItemClickListener itemClickListener) {
//                super(itemView);
//                this.itemClickListener = itemClickListener;
//                itemView.setOnClickListener(this);
//                image = (ImageView) itemView.findViewById(R.id.image);
//            }
//
//            @Override
//            public void onClick(View v) {
//                if (itemClickListener == null) return;
//                itemClickListener.click(itemView, getAdapterPosition());
//            }
//
//        }
//    }
}
