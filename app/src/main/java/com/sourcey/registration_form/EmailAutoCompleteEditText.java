package com.sourcey.registration_form;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

/**
 * Auto Complete Edit Text for user email
 */
public class EmailAutoCompleteEditText extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    static String[] domains = new String[]{
            /* Default domains included */
            "aol.com", "att.net", "comcast.net", "facebook.com", "gmail.com", "gmx.com", "googlemail.com",
            "google.com", "hotmail.com", "hotmail.co.uk", "mac.com", "me.com", "mail.com", "msn.com",
            "live.com", "sbcglobal.net", "verizon.net", "yahoo.com", "yahoo.co.uk",

            /* Other global domains */
            "email.com", "games.com" /* AOL */, "gmx.net", "hush.com", "hushmail.com", "inbox.com",
            "lavabit.com", "love.com" /* AOL */, "pobox.com", "rocketmail.com" /* Yahoo */,
            "safe-mail.net", "wow.com" /* AOL */, "ygm.com" /* AOL */, "ymail.com" /* Yahoo */, "zoho.com", "fastmail.fm",

            /* United States ISP domains */
            "bellsouth.net", "charter.net", "cox.net", "earthlink.net", "juno.com",

            /* British ISP domains */
            "btinternet.com", "virginmedia.com", "blueyonder.co.uk", "freeserve.co.uk", "live.co.uk",
            "ntlworld.com", "o2.co.uk", "orange.net", "sky.com", "talktalk.co.uk", "tiscali.co.uk",
            "virgin.net", "wanadoo.co.uk", "bt.com",

            /* Domains used in Asia */
            "sina.com", "qq.com", "naver.com", "hanmail.net", "daum.net", "nate.com", "yahoo.co.jp", "yahoo.co.kr", "yahoo.co.id", "yahoo.co.in", "yahoo.com.sg", "yahoo.com.ph",

            /* French ISP domains */
            "hotmail.fr", "live.fr", "laposte.net", "yahoo.fr", "wanadoo.fr", "orange.fr", "gmx.fr", "sfr.fr", "neuf.fr", "free.fr",

            /* German ISP domains */
            "gmx.de", "hotmail.de", "live.de", "online.de", "t-online.de" /* T-Mobile */, "web.de", "yahoo.de",

            /* Russian ISP domains */
            "mail.ru", "rambler.ru", "yandex.ru",

            /* Belgian ISP domains */
            "hotmail.be", "live.be", "skynet.be", "voo.be", "tvcablenet.be",

            /* Argentinian ISP domains */
            "hotmail.com.ar", "live.com.ar", "yahoo.com.ar", "fibertel.com.ar", "speedy.com.ar", "arnet.com.ar",

            /* Domains used in Mexico */
            "hotmail.com", "gmail.com", "yahoo.com.mx", "live.com.mx", "yahoo.com", "hotmail.es", "live.com", "hotmail.com.mx", "prodigy.net.mx", "msn.com",
            // TODO delete this
            /* Custom domains*/
            "zalupa.com", "daun.com", "debil.com"
    };

    public EmailAutoCompleteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        Log.d("myDog",  "Constructor 1");
    }

    public EmailAutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        Log.d("myDog",  "Constructor 2");
    }

    public EmailAutoCompleteEditText(Context context) {
        super(context);
        init();
        Log.d("myDog",  "Constructor 3");
    }

    public void init() {
        setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("myDog",  "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("myDog",  "onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("myDog",  "afterTextChanged");
                Log.d("myDog",  s.toString());
                if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    setError(null);
                } else {
                    setError("Please enter a valid email address");
                }

                ArrayList<String> list;
                String str = s.toString();

                //If we have an @ sign + 1 char  then auto complete on a list of domains
                int atSignPosition = str.indexOf('@');
                Log.d("myDog", "atSignPosition > 1: " + (atSignPosition > 1));
                Log.d("myDog", "atSignPosition < (str.length() + 1): " + (atSignPosition < (str.length() + 1)));
                if (atSignPosition > 1
                        && atSignPosition < (str.length() + 1)) {
                    String lookup = str.substring(atSignPosition + 1);
                    String prefix = str.substring(0, atSignPosition + 1);
                    Log.d("myDog", "lookup " + (lookup));
                    Log.d("myDog", "prefix " + (prefix));
                    list = new ArrayList<String>();
                    for (String domain : domains) {
                        String option = prefix + domain;
                        if (option.startsWith(s.toString())) {
                            list.add(option);
                        }
                    }
                } else {
                    Log.d("myDog", "Strange block execution");
                    //Before we have an @ sign try and pull the email address from the device
                    Account[] accounts = AccountManager.get(getContext()).getAccountsByType("com.google");
                    list = new ArrayList<>();
                    for (Account account : accounts) {
                        list.add(account.name);
                    }
                }
                setAdapter(new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        list));


            }
        });
    }
}