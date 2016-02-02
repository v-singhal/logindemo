package photos.vbstudio.login.adpaters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import photos.vbstudio.login.pojo.EmbeddedFragmentData;


public class IntroPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<EmbeddedFragmentData> embeddedFragmentDataList;

    public IntroPagerAdapter(FragmentManager fm, Context context, List<EmbeddedFragmentData> embeddedFragmentDataList) {
        super(fm);
        this.context = context;
        this.embeddedFragmentDataList = embeddedFragmentDataList;
    }

    @Override
    public int getCount() {
        return embeddedFragmentDataList.size();
    }

    @Override
    public Fragment getItem(int position) {

        return embeddedFragmentDataList.get(position).getBaseFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return embeddedFragmentDataList.get(position).getFragmentTitle();
    }

}
