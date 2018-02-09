package moran_company.honestgram.fragments.product_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.fragmentargs.bundler.ParcelerArgsBundler;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Urls;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.fragments.base.BaseMvpFragment;
import moran_company.honestgram.ui.SpecialOffersView;

/**
 * Created by roman on 26.01.2018.
 */

@FragmentWithArgs
public class ProductDetailFragment extends BaseMvpFragment<ProductDetailMvp.Presenter> implements ProductDetailMvp.View {

    @Arg(bundler = ParcelerArgsBundler.class)
    Goods mGood;

    @BindView(R.id.sendMessageView)
    LinearLayout sendMessageView;
    @BindView(R.id.newMessageEditText)
    EditText newMessageEditText;

    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.datePublish)
    TextView datePublish;
    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.photos)
    SpecialOffersView photos;

    @Override
    protected ProductDetailMvp.Presenter createPresenter() {
        return new ProductDetailPresenter(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_product_detail;
    }

    public static ProductDetailFragment newInstance(Goods good) {
        ProductDetailFragment fragment = new ProductDetailFragmentBuilder(good).build();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Users user = PreferencesData.INSTANCE.getUser();
        if (mGood.getUrls() != null) {
            photos.setGoods(mGood.getUrls(), false);
        }else
            photos.setGoods(new ArrayList<Urls>(Arrays.asList(new Urls(mGood.getUrl()))), false);

        price.setText(getString(R.string.concrete_price, mGood.getPrice()));
        title.setText(mGood.getTitle());
        description.setText(mGood.getDescription());
        if (mGood.getOwnerId() == user.getId()){
            sendMessageView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.sendMessage)
    void sendMessage(){
        String message = newMessageEditText.getText().toString();
        if (!message.isEmpty()){
            mPresenter.checkChat(mGood,message);
        }
    }

    @Override
    public void showChat(Chats chat) {
        mBaseActivity.showChatFragment(chat);
    }


}
