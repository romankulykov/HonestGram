package moran_company.honestgram.fragments.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.fragmentargs.bundler.ParcelerArgsBundler;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.adapters.ChatAdapter;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.fragments.base.BaseMvpFragment;
import moran_company.honestgram.utility.ImageFilePath;

import static android.app.Activity.RESULT_OK;

/**
 * Created by roman on 14.01.2018.
 */

@FragmentWithArgs
public class ChatFragment extends BaseMvpFragment<ChatMvp.Presenter> implements ChatMvp.View {

    @BindView(R.id.chat)
    RecyclerView chat;
    @BindView(R.id.newMessageEditText)
    EditText newMessageEditText;
    @BindView(R.id.attachedPhoto)
    ImageView attachedPhoto;

    private String attachedPhotoURL;


    @Arg(bundler = ParcelerArgsBundler.class)
    public Chats chats;

    private List<Dialogs> dialogs = new ArrayList<>();

    private ChatAdapter chatAdapter = new ChatAdapter();

    @Override
    protected ChatMvp.Presenter createPresenter() {
        return new ChatPresenter(this, chats.getId());
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat_detail;
    }

    public static ChatFragment newInstance(Chats chats) {
        ChatFragment fragment = new ChatFragmentBuilder(chats).build();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chat.setAdapter(chatAdapter);
        chatAdapter.setOnPhotoClickListener(url -> mBaseActivity.showZoomPhotoFragment(url));
        showDialogs(chats.getDialogs());
        //mPresenter.loadMessages();
    }

    @Override
    public void showDialogs(List<Dialogs> dialogsList) {
        if (dialogsList != null) {
            this.dialogs = dialogsList;
            chatAdapter.setItems(dialogsList);
            newMessageEditText.setText("");
            chat.getLayoutManager().scrollToPosition(chatAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void showUrlPhoto(String url) {
        GlideApp.with(getContext())
                .load(url)
                .into(attachedPhoto);
        attachedPhotoURL = url;
        attachedPhoto.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.sendMessage)
    void sendMessage() {
        List<Long> msgIds = new ArrayList<>();
        long maxDialogId;
        for (int i = 0; i < dialogs.size(); i++) {
            msgIds.add(dialogs.get(i).getMessage_id());
        }
        maxDialogId = msgIds.size()==0?0:Collections.max(msgIds);
        String message = newMessageEditText.getText().toString();
        if (!message.isEmpty())
            mPresenter.sendMessage(message, chats.getId(), maxDialogId, attachedPhotoURL);
        else showToast(R.string.empty_fields);
    }

    @Override
    public void successSend() {
        attachedPhotoURL = "";
        attachedPhoto.setVisibility(View.GONE);
    }

    @OnClick(R.id.attachPhoto)
    void attachPhoto(){
        CropImage.activity()
                //.setRequestedSize(Constants.PROFILE_WIDTH, Constants.PROFILE_HEIGHT)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(132, 170)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                String selectedImagePath;
                Uri imageUri = result.getUri();

                selectedImagePath = ImageFilePath.getPath(getContext(), imageUri);
                Log.i("Image File Path", "" + selectedImagePath);
                mPresenter.attachImage(selectedImagePath,chats.getId());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
