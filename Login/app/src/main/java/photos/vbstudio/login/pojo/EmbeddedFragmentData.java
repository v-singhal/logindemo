package photos.vbstudio.login.pojo;


import photos.vbstudio.login.fragments.BaseFragment;

public class EmbeddedFragmentData {

    private BaseFragment baseFragment;
    private String fragmentTitle;

    public BaseFragment getBaseFragment() {
        return baseFragment;
    }

    public void setBaseFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

    public String getFragmentTitle() {
        return fragmentTitle;
    }

    public void setFragmentTitle(String fragmentTitle) {
        this.fragmentTitle = fragmentTitle;
    }
}
