package com.nucleustech.mymentor.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.model.ChatModel;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by ritwik.rai
 */
public class ChatFirebaseAdapter extends FirebaseRecyclerAdapter<ChatModel, ChatFirebaseAdapter.MyChatViewHolder> {

    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_IMG = 2;
    private static final int LEFT_MSG_IMG = 3;
    private static final int RIGHT_MSG_FILE = 4;
    private static final int LEFT_MSG_FILE = 5;

    private ClickListenerChatFirebase mClickListenerChatFirebase;

    private String nameUser;
    private String userEmail;

    //Admin
    public ChatFirebaseAdapter(DatabaseReference ref, String nameUser, ClickListenerChatFirebase mClickListenerChatFirebase, String studentEmail, String userEmail) {

        super(ChatModel.class, R.layout.item_message_left, ChatFirebaseAdapter.MyChatViewHolder.class, ref.orderByChild("keyMap").equalTo("admin" + studentEmail));
        this.nameUser = nameUser;
        this.userEmail = userEmail;
        this.mClickListenerChatFirebase = mClickListenerChatFirebase;
    }

    //Student
    public ChatFirebaseAdapter(DatabaseReference ref, String nameUser, ClickListenerChatFirebase mClickListenerChatFirebase, String email, boolean val) {

        super(ChatModel.class, R.layout.item_message_left, ChatFirebaseAdapter.MyChatViewHolder.class, ref.orderByChild("keyMap").equalTo("admin" + email));

        this.nameUser = nameUser;
        this.userEmail = email;
        this.mClickListenerChatFirebase = mClickListenerChatFirebase;
    }

    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new MyChatViewHolder(view);
        } else if (viewType == LEFT_MSG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new MyChatViewHolder(view);
        } else if (viewType == RIGHT_MSG_IMG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_img, parent, false);
            return new MyChatViewHolder(view);
        } else if (viewType == LEFT_MSG_IMG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_img, parent, false);
            return new MyChatViewHolder(view);
        } else if (viewType == RIGHT_MSG_FILE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_file, parent, false);
            return new MyChatViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_file, parent, false);
            return new MyChatViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel model = getItem(position);
        if (model.getMapModel() != null) {
            if (model.getSenderEmail().equals(userEmail)) {
                return RIGHT_MSG_IMG;
            } else {
                return LEFT_MSG_IMG;
            }
        } else if (model.getFile() != null) {
            Log.e("TAG", "Type: " + model.getFile().getType());


            if (model.getFile().getType().equals("img")) {
                if (model.getSenderEmail().equals(userEmail)) {
                    return RIGHT_MSG_IMG;
                } else {
                    return LEFT_MSG_IMG;
                }
            } else {
                if (model.getSenderEmail().equals(userEmail)) {
                    return RIGHT_MSG_FILE;
                } else {
                    return LEFT_MSG_FILE;
                }
            }

        } else if (model.getSenderEmail().equals(userEmail)) {
            return RIGHT_MSG;
        } else {
            return LEFT_MSG;
        }
    }

    @Override
    protected void populateViewHolder(MyChatViewHolder viewHolder, ChatModel model, int position) {
        viewHolder.setIvUser(model.getUserModel().getPhoto_profile());
        viewHolder.setTxtMessage(model.getMessage());
        viewHolder.setTvTimestamp(model.getTimeStamp());
        viewHolder.tvIsLocation(View.GONE);
        if (model.getFile() != null) {
            if (model.getFile().getType().equals("img")) {
                viewHolder.tvIsLocation(View.GONE);
                viewHolder.setIvChatPhoto(model.getFile().getUrl_file());
            } else if (model.getFile().getType().equals("file")) {
                viewHolder.tvIsLocation(View.GONE);
                viewHolder.setFileChatPhoto(model.getFile().getUrl_file());
            }
        } else if (model.getMapModel() != null) {
            viewHolder.setIvChatPhoto(com.nucleustech.mymentor.util.Util.local(model.getMapModel().getLatitude(), model.getMapModel().getLongitude()));
            viewHolder.tvIsLocation(View.VISIBLE);
        }
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTimestamp, tvLocation, tv_filename;
        EmojiconTextView txtMessage;
        ImageView ivUser, ivChatPhoto;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = (TextView) itemView.findViewById(R.id.timestamp);
            txtMessage = (EmojiconTextView) itemView.findViewById(R.id.txtMessage);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            ivChatPhoto = (ImageView) itemView.findViewById(R.id.img_chat);
            ivUser = (ImageView) itemView.findViewById(R.id.ivUserChat);
            tv_filename = (TextView) itemView.findViewById(R.id.tv_filename);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ChatModel model = getItem(position);
            if (model.getMapModel() != null) {
                mClickListenerChatFirebase.clickImageMapChat(view, position, model.getMapModel().getLatitude(), model.getMapModel().getLongitude());
            } else {
                if (model.getFile().getType().equals("img"))
                    mClickListenerChatFirebase.clickImageChat(view, position, model.getUserModel().getName(), model.getUserModel().getPhoto_profile(), model.getFile().getUrl_file());
                else
                    mClickListenerChatFirebase.clickFileChat(view, position, model.getUserModel().getName(), model.getUserModel().getPhoto_profile(), model.getFile().getUrl_file());
            }
        }

        public void setTxtMessage(String message) {
            if (txtMessage == null) return;
            txtMessage.setText(message);
        }

        public void setIvUser(String urlPhotoUser) {
            if (ivUser == null) return;
            Glide.with(ivUser.getContext()).load(urlPhotoUser).centerCrop().transform(new CircleTransform(ivUser.getContext())).override(40, 40).into(ivUser);
        }

        public void setTvTimestamp(String timestamp) {
            if (tvTimestamp == null) return;
            tvTimestamp.setText(converteTimestamp(timestamp));
        }

        public void setIvChatPhoto(String url) {
            if (ivChatPhoto == null) return;
            Glide.with(ivChatPhoto.getContext()).load(url)
                    .override(100, 100)
                    .fitCenter()
                    .into(ivChatPhoto);
            ivChatPhoto.setOnClickListener(this);
        }

        public void setFileChatPhoto(String url) {
            Log.e("Image Url", "Image URL: " + url);
            //url= "https://firebasestorage.googleapis.com/v0/b/developer-raisahab.appspot.com/o/files%2Fic_doc.png?alt=media&token=d1c7896c-d593-416e-a5fb-de75923ae68e";
            //ivChatPhoto.setImageDrawable(R.drawable.ic_doc);
            if (ivChatPhoto == null) return;

            String fileName=getBetweenStrings(url, "_mentorfile_", "_gallery");
            //ivChatPhoto.setImageBitmap();
            //Glide.with(ivChatPhoto.getContext()).load(url).override(60, 60).fitCenter().into(ivChatPhoto);
            tv_filename.setText(""+fileName);
            ivChatPhoto.setOnClickListener(this);
        }

        public void tvIsLocation(int visible) {
            if (tvLocation == null) return;
            tvLocation.setVisibility(visible);
        }

    }

    private CharSequence converteTimestamp(String mileSegundos) {
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(mileSegundos), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }

    /**
     * Get text between two strings. Passed limiting strings are not
     * included into result.
     *
     * @param text     Text to search in.
     * @param textFrom Text to start cutting from (exclusive).
     * @param textTo   Text to stop cuutting at (exclusive).
     */
    public static String getBetweenStrings(String text, String textFrom, String textTo) {

        String result = "";

        // Cut the beginning of the text to not occasionally meet a
        // 'textTo' value in it:
        result = text.substring(text.indexOf(textFrom) + textFrom.length(), text.length());

        // Cut the excessive ending of the text:
        result = result.substring(0, result.indexOf(textTo));
        String newResult = result.replaceAll("\\+", " ");
       // Log.e("newResult","newResult: "+newResult);

        if (result.contains("/"))
            return "";
        else
            return newResult;
    }
}
