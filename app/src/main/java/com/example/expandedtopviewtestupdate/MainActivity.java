package com.example.expandedtopviewtestupdate;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MyRecyclerView.AppBarTracking {
    private MyRecyclerView mNestedView;
    private int mAppBarOffset = 0;
    private boolean mAppBarIdle = true;
    private int mAppBarMaxOffset = 0;
    private AppBarLayout mAppBar;
    private boolean mIsExpanded = false;
    private ImageView mArrowImageView;
    private TextView mSmallerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAppBar = findViewById(R.id.app_bar);
        mAppBar.post(new Runnable() {
            @Override
            public void run() {
                mAppBarMaxOffset = -mAppBar.getTotalScrollRange();
            }
        });
        mArrowImageView = findViewById(R.id.arrowImageView);
        mSmallerView = findViewById(R.id.smallerView);
        mNestedView = findViewById(R.id.nestedView);
        mNestedView.setAppBarTracking(this);
        mNestedView.setLayoutManager(new LinearLayoutManager(this));
        mNestedView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(
                    LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText("ITEM " + position);
            }

            @Override
            public int getItemCount() {
                return 100;
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
                if (mAppBarIdle) {
                    setExpandAndCollapseEnabled(mIsExpanded);
                }
                mSmallerView.setAlpha((float) -verticalOffset / totalScrollRange);
            }
        });

        mArrowImageView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                mIsExpanded = !mIsExpanded;
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

//    @Override
//    public int getExpansionLeft() {
//        return mAppBar.getBottom() - mAppBar.getHeight();
//    }
}
