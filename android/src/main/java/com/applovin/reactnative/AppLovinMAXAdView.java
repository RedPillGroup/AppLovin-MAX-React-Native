package com.applovin.reactnative;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;

import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdkUtils;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.view.ReactViewGroup;

/**
 * Created by Thomas So on September 27 2020
 */
class AppLovinMAXAdView
        extends ReactViewGroup
{
    private final ThemedReactContext reactContext;

    private MaxAdView adView;

    public AppLovinMAXAdView(final Context context)
    {
        super( context );

        this.reactContext = (ThemedReactContext) context;
    }

    @Override
    public void requestLayout()
    {
        super.requestLayout();

        // https://stackoverflow.com/a/39838774/5477988 - This is required to ensure ad refreshes render correctly in RN Android due to known issue
        if ( adView != null )
        {
            adView.measure(
                    MeasureSpec.makeMeasureSpec( getMeasuredWidth(), MeasureSpec.EXACTLY ),
                    MeasureSpec.makeMeasureSpec( getMeasuredHeight(), MeasureSpec.EXACTLY )
            );
            adView.layout( 0, 0, adView.getMeasuredWidth(), adView.getMeasuredHeight() );
        }
    }

    public void maybeAttachAdView(final String adUnitId, final MaxAdFormat adFormat)
    {
        Activity currentActivity = reactContext.getCurrentActivity();
        if ( currentActivity == null )
        {
            AppLovinMAXModule.e( "Unable to attach AdView - no current Activity found" );
            return;
        }

        currentActivity.runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                // If ad unit id and format has been set - create and attach AdView
                if ( !TextUtils.isEmpty( adUnitId ) && adFormat != null )
                {
                    adView = AppLovinMAXModule.getInstance().retrieveAdView( adUnitId, adFormat, "" );
                    adView.loadAd();

                    addView( adView );

                    // Set the height of the banner ad based on the device type.
                    AppLovinMAXModule.AdViewSize adViewSize = AppLovinMAXModule.getAdViewSize( adFormat );
                    int widthPx = AppLovinSdkUtils.dpToPx( reactContext, adViewSize.widthDp );
                    int heightPx = AppLovinSdkUtils.dpToPx( reactContext, adViewSize.heightDp );

                    ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) adView.getLayoutParams();
                    layoutParams.width = widthPx;
                    layoutParams.height = heightPx;
                    adView.setGravity( Gravity.CENTER );
                }
            }
        } );
    }
}
