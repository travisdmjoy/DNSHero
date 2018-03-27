package com.gianlu.dnshero;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gianlu.commonutils.Dialogs.ActivityWithDialog;
import com.gianlu.dnshero.DNSRecords.DNSRecordFragment;
import com.gianlu.dnshero.Domain.DiagnosticFragment;
import com.gianlu.dnshero.Domain.NameserversFragment;
import com.gianlu.dnshero.Domain.PagerAdapter;
import com.gianlu.dnshero.Domain.RootNameserverFragment;
import com.gianlu.dnshero.NetIO.Domain;

public class DomainActivity extends ActivityWithDialog {

    public static void startActivity(Context context, Domain domain) {
        context.startActivity(new Intent(context, DomainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra("domain", domain));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.domain, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain);

        setSupportActionBar((Toolbar) findViewById(R.id.domain_toolbar));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager pager = findViewById(R.id.domain_pager);
        TabLayout tabs = findViewById(R.id.domain_tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Domain domain = (Domain) getIntent().getSerializableExtra("domain");
        if (domain == null) {
            onBackPressed();
            return;
        }

        setTitle(getString(R.string.appName_domain, domain.name));

        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(),
                DiagnosticFragment.getInstance(this, domain),
                RootNameserverFragment.getInstance(this, domain),
                NameserversFragment.getInstance(this, domain),
                DNSRecordFragment.getAInstance(this, domain),
                DNSRecordFragment.getAAAAInstance(this, domain),
                DNSRecordFragment.getCNAMEInstance(this, domain),
                DNSRecordFragment.getMXInstance(this, domain),
                DNSRecordFragment.getTXTInstance(this, domain),
                DNSRecordFragment.getSOAInstance(this, domain)));

        tabs.setupWithViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.domain_preferences:
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoadingActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
