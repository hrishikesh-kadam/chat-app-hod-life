package com.example.android.chatapphodlife;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.chatapphodlife.data.ChatContract.ChatEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hrishikesh Kadam on 16/11/2017
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final String LOG_TAG = ChatAdapter.class.getSimpleName();
    private static final int EMPTY_VIEW = 1;
    private static final int SENDER_VIEW = 2;
    private static final int RECEIVER_VIEW = 3;
    private Context context;
    private Cursor cursor;

    public ChatAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewHolder viewHolder = null;

        if (viewType == EMPTY_VIEW) {

            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.empty_chat_view, parent, false);
            viewHolder = new ViewHolder(itemView);

        } else if (viewType == SENDER_VIEW) {

            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.sender_bubble, parent, false);
            viewHolder = new SenderViewHolder(itemView);

        } else if (viewType == RECEIVER_VIEW) {

            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.receiver_bubble, parent, false);
            viewHolder = new ReceiverViewHolder(itemView);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if (viewType == SENDER_VIEW) {

            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.textViewSenderMessage.setText(
                    cursor.getString(cursor.getColumnIndex(ChatEntry.COLUMN_MESSAGE)));

        } else if (viewType == RECEIVER_VIEW) {

            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.textViewReceiverMessage.setText(
                    cursor.getString(cursor.getColumnIndex(ChatEntry.COLUMN_MESSAGE)));
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (cursor == null || cursor.getCount() == 0)
            return EMPTY_VIEW;

        else {
            cursor.moveToPosition(cursor.getCount()-1 - position);
            String recipient = cursor.getString(cursor.getColumnIndex(ChatEntry.COLUMN_RECIPIENT));

            if (recipient.equals(ChatEntry.RECIPIENT_SENDER))
                return SENDER_VIEW;
            else if (recipient.equals(ChatEntry.RECIPIENT_RECEIVER))
                return RECEIVER_VIEW;
            else
                throw new UnsupportedOperationException("Unknown position found at " + position);
        }
    }

    @Override
    public int getItemCount() {

        if (cursor == null || cursor.getCount() == 0)
            return 1;
        else
            return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class SenderViewHolder extends ViewHolder {

        @BindView(R.id.textViewSenderMessage)
        TextView textViewSenderMessage;

        public SenderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ReceiverViewHolder extends ViewHolder {

        @BindView(R.id.textViewReceiverMessage)
        TextView textViewReceiverMessage;

        public ReceiverViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
