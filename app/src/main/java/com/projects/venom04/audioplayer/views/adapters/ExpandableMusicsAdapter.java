package com.projects.venom04.audioplayer.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.models.pojo.Audio;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venom on 25/09/2017.
 */

public class ExpandableMusicsAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mListHeaderData;
    private HashMap<String, List<Audio>> mListChildData;

    static class ViewHeaderHolder {
        @BindView(R.id.text_view_alphabet)
        TextView mTvAlphabet;

        public ViewHeaderHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewChildHolder {
        @BindView(R.id.text_view_title)
        TextView mTvTitle;
        @BindView(R.id.text_view_artist)
        TextView mTvArtist;
        @BindView(R.id.image_view_state)
        ImageView mIvState;

        public ViewChildHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public ExpandableMusicsAdapter(Context context,
                                   List<String> listHeaderData,
                                   HashMap<String, List<Audio>> listChildData) {
        mContext = context;
        mListHeaderData = listHeaderData;
        mListChildData = listChildData;
    }

    @Override
    public int getGroupCount() {
        return mListHeaderData.size();
    }

    @Override
    public int getChildrenCount(int pos) {
        return mListChildData.get(mListHeaderData.get(pos)).size();
    }

    @Override
    public Object getGroup(int pos) {
        return mListHeaderData.get(pos);
    }

    @Override
    public Object getChild(int groupPos, int childPos) {
        return mListChildData.get(mListHeaderData.get(groupPos)).get(childPos);
    }

    @Override
    public long getGroupId(int groupPos) {
        return groupPos;
    }

    @Override
    public long getChildId(int groupPos, int childPos) {
        return childPos;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPos, boolean b, View convertView, ViewGroup viewGroup) {
        ViewHeaderHolder holder;
        String header = (String) getGroup(groupPos);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_item_music, viewGroup, false);
            holder = new ViewHeaderHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHeaderHolder) convertView.getTag();
        }

        holder.mTvAlphabet.setText(header);

        return convertView;
    }

    @Override
    public View getChildView(int groupPos, int childPos, boolean b, View convertView, ViewGroup viewGroup) {
        ViewChildHolder holder;
        Audio audio = (Audio) getChild(groupPos, childPos);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_music, viewGroup, false);
            holder = new ViewChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewChildHolder) convertView.getTag();
        }

        holder.mTvTitle.setText(audio.getTitle());
        holder.mTvArtist.setText(audio.getArtist());
//        holder.mIvState.setImageResource();

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public interface MusicAdapterListener {
        void onMusicItemSelected(Audio audio);
    }
}
