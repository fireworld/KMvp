package cc.colorcat.mvp.extension.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cc.colorcat.mvp.extension.L;
import cc.colorcat.netbird4.Request;
import cc.colorcat.netbird4.cache.CacheInterceptor;
import cc.colorcat.netbird4.cache.DiskCache;

/**
 * Created by cxx on 18-2-6.
 * xx.ch@outlook.com
 */
public final class AndroidCacheInterceptor extends CacheInterceptor {
    public static AndroidCacheInterceptor create(Context context, List<String> ignoredQueryNames) {
        try {
            File cacheDirectory = context.getCacheDir();
            long cacheSize = (long) Math.min(20 * 1024 * 1024, cacheDirectory.getUsableSpace() * 0.1);
            DiskCache cache = DiskCache.open(cacheDirectory, cacheSize);
            return new AndroidCacheInterceptor(context, cache, ignoredQueryNames);
        } catch (IOException e) {
            L.e(e);
            return null;
        }
    }

    private volatile boolean hasAvailableNetwork = true;

    private AndroidCacheInterceptor(Context context, DiskCache diskCache, List<String> ignoredQueryNames) {
        super(diskCache, ignoredQueryNames);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(new NetworkStateReceiver(), filter);
    }

    @Override
    protected boolean forceLoadCached() {
        return !hasAvailableNetwork;
    }

    @Override
    protected String createStableKey(Request request) {
        final String url = request.url();
        final int queryIndex = url.indexOf('?');
        if (queryIndex == -1) return url;

        final Uri uri = Uri.parse(url);
        final Set<String> queryNames = new LinkedHashSet<>(uri.getQueryParameterNames());
        queryNames.removeAll(ignoredQueryNames);
        final List<String> stableNames = new ArrayList<>(queryNames);
        int size = stableNames.size();
        if (size == 0) return url.substring(0, queryIndex);

        Collections.sort(stableNames);
        final StringBuilder stableUrl = new StringBuilder(url.substring(0, queryIndex)).append('?');
        for (int i = 0; i < size; ++i) {
            if (i != 0) stableUrl.append('&');
            String name = stableNames.get(i);
            stableUrl.append(name).append('=').append(uri.getQueryParameter(name));
        }
        return stableUrl.toString();
    }

    private class NetworkStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            hasAvailableNetwork = hasAvailableNetwork(context);
        }
    }

    private static boolean hasAvailableNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnectedOrConnecting();
        }
        return false;
    }
}
