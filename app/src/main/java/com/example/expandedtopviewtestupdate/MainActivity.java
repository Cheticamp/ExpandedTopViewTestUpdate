package com.example.expandedtopviewtestupdate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
    implements MyRecyclerView.AppBarTracking {
    private MyRecyclerView mNestedView;
    private int mAppBarOffset = 0;
    private boolean mAppBarIdle = true;
    private int mAppBarMaxOffset = 0;
    private AppBarLayout mAppBar;
    private boolean mIsExpanded = false;
    private ImageView mArrowImageView;
    private LinearLayout mSmallLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LinearLayout expandCollapse;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        expandCollapse = findViewById(R.id.expandCollapseButton);
        mArrowImageView = findViewById(R.id.arrowImageView);
        mNestedView = findViewById(R.id.nestedView);
        mAppBar = findViewById(R.id.app_bar);
        mSmallLayout = findViewById(R.id.smallLayout);

        // Log when the small text view is clicked
        findViewById(R.id.smallTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "<<<<click small layout");
            }
        });

        // Log when the big text view is clicked.
        findViewById(R.id.largeTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "<<<<click big view");
            }
        });

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mAppBar.post(new Runnable() {
            @Override
            public void run() {
                mAppBarMaxOffset = -mAppBar.getTotalScrollRange();

                CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams();
                MyAppBarBehavior behavior = (MyAppBarBehavior) lp.getBehavior();
                // Only allow drag-to-open if the drag touch is on the toolbar.
                // Once open, all drags are allowed.
                if (behavior != null) {
                    behavior.setCanOpenBottom(findViewById(R.id.toolbar).getHeight());
                }
            }
        });

        mNestedView.setAppBarTracking(this);
        mNestedView.setLayoutManager(new LinearLayoutManager(this));
        mNestedView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(
                    LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false));
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(android.R.id.text1))
                    .setText("Item " + position);
            }

            @Override
            public int getItemCount() {
                return 200;
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                public ViewHolder(View view) {
                    super(view);
                }
            }
        });

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mAppBarOffset = verticalOffset;
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                float progress = (float) (-verticalOffset) / (float) totalScrollRange;
                mArrowImageView.setRotation(-progress * 180);
                mIsExpanded = verticalOffset == 0;
                mAppBarIdle = mAppBarOffset >= 0 || mAppBarOffset <= mAppBarMaxOffset;
                float alpha = (float) -verticalOffset / totalScrollRange;
                mSmallLayout.setAlpha(alpha);

                // If the small layout is not visible, make it officially invisible so
                // it can't receive clicks.
                if (alpha == 0) {
                    mSmallLayout.setVisibility(View.INVISIBLE);
                } else if (mSmallLayout.getVisibility() == View.INVISIBLE) {
                    mSmallLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        expandCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExpandAndCollapseEnabled(true);
                if (mIsExpanded) {
                    setExpandAndCollapseEnabled(false);
                }
                mIsExpanded = !mIsExpanded;
                mNestedView.stopScroll();
                mAppBar.setExpanded(mIsExpanded, true);
            }
        });
    }

    private void setExpandAndCollapseEnabled(boolean enabled) {
        if (mNestedView.isNestedScrollingEnabled() != enabled) {
            mNestedView.setNestedScrollingEnabled(enabled);
        }
    }

    @Override
    public boolean isAppBarExpanded() {
        return mAppBarOffset == 0;
    }

    @Override
    public boolean isAppBarIdle() {
        return mAppBarIdle;
    }

    private static final String TAG = "MainActivity";
}
