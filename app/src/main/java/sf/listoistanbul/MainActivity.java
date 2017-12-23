package sf.listoistanbul;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import sf.listoistanbul.fragment.HomeFragment;
import sf.listoistanbul.fragment.InformationFragment;
import sf.residemenu.ResideMenu;
import sf.residemenu.ResideMenuItem;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ResideMenu resideMenu;
    private ResideMenuItem rMenuHome, rMenuAbout, rMenuInformation;
    private ImageView toolbar_left_menu;
    private customfonts.AppBarTextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_left_menu = (ImageView) findViewById(R.id.toolbar_left_menu);
        toolbar_title = (customfonts.AppBarTextView) findViewById(R.id.toolbar_title);
        toolbar_title.bringToFront();

        setUpMenu();
        if (savedInstanceState == null) {
            changeFragment(new HomeFragment());
        }

    }

    private void setUpMenu() {
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.img_back3);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.8f);
        resideMenu.setUse3D(true);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        rMenuHome = new ResideMenuItem(this, R.drawable.ic_home_white_24dp, "ANASAYFA");
        rMenuInformation = new ResideMenuItem(this, R.drawable.ic_group_work_white_24dp, "BİLGİLENDİRME");
        rMenuAbout = new ResideMenuItem(this, R.drawable.ic_whatshot_white_24dp, "HAKKINDA");

        rMenuHome.setOnClickListener(this);
        rMenuInformation.setOnClickListener(this);
        rMenuAbout.setOnClickListener(this);

        resideMenu.addMenuItem(rMenuHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(rMenuInformation, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(rMenuAbout, ResideMenu.DIRECTION_LEFT);
        toolbar_left_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == rMenuHome) {
            changeFragment(new HomeFragment());
        } else if (view == rMenuInformation) {
            changeFragment(new InformationFragment());
        } else if (view == rMenuAbout) {

        }

        resideMenu.closeMenu();
    }

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
