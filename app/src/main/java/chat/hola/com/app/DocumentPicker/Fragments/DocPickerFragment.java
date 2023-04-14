package chat.hola.com.app.DocumentPicker.Fragments;

/**
 * Created by moda on 22/08/17.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.common.util.Predicate;
import com.google.android.material.tabs.TabLayout;
import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import chat.hola.com.app.DocumentPicker.Adapters.SectionsPagerAdapter;
import chat.hola.com.app.DocumentPicker.Cursors.LoaderCallbacks.FileResultCallback;
import chat.hola.com.app.DocumentPicker.Models.Document;
import chat.hola.com.app.DocumentPicker.Models.FileType;
import chat.hola.com.app.DocumentPicker.PickerManager;
import chat.hola.com.app.DocumentPicker.Utils.MediaStoreHelper;
import chat.hola.com.app.DocumentPicker.Utils.TabLayoutHelper;
import chat.hola.com.app.DocumentPicker.Utils.Utils;

//import com.android.internal.util.Predicate;


public class DocPickerFragment extends BaseFragment {

    private static final String TAG = DocPickerFragment.class.getSimpleName();

    TabLayout tabLayout;
    ViewPager viewPager;
    private ArrayList<String> selectedPaths;
    private ProgressBar progressBar;
    private DocPickerFragmentListener mListener;

    public DocPickerFragment() {
        // Required empty public constructor
    }

    public interface DocPickerFragmentListener {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doc_picker, container, false);
    }

    public static DocPickerFragment newInstance(ArrayList<String> selectedPaths) {
        DocPickerFragment docPickerFragment = new DocPickerFragment();
        docPickerFragment.selectedPaths = selectedPaths;
        return docPickerFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DocPickerFragmentListener) {
            mListener = (DocPickerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DocPickerFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews(view);
        initView();
    }

    private void initView() {
        setUpViewPager();
        setData();
    }

    private void setViews(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setData() {
        MediaStoreHelper.getDocs(getActivity(), new FileResultCallback<Document>() {
            @Override
            public void onResultCallback(List<Document> files) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                setDataOnFragments(files);
            }
        });
    }

    private void setDataOnFragments(List<Document> files) {
        SectionsPagerAdapter sectionsPagerAdapter = (SectionsPagerAdapter) viewPager.getAdapter();
        if (sectionsPagerAdapter != null) {
            for (int index = 0; index < sectionsPagerAdapter.getCount(); index++) {
                DocFragment docFragment = (DocFragment) getChildFragmentManager()
                        .findFragmentByTag(
                                "android:switcher:" + R.id.viewPager + ":" + index);
                if (docFragment != null) {
                    FileType fileType = docFragment.getFileType();
                    if (fileType != null)
                        docFragment.updateList(filterDocuments(fileType.extensions, files));
                }
            }
        }
    }

    private void setUpViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        ArrayList<FileType> supportedTypes = PickerManager.getInstance().getFileTypes();
        for (int index = 0; index < supportedTypes.size(); index++) {
            try{
            adapter.addFragment(DocFragment.newInstance(supportedTypes.get(index)), supportedTypes.get(index).title);

            }catch(IllegalStateException e){e.printStackTrace();}
            }

        viewPager.setOffscreenPageLimit(supportedTypes.size());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        TabLayoutHelper mTabLayoutHelper = new TabLayoutHelper(tabLayout, viewPager);
        mTabLayoutHelper.setAutoAdjustTabModeEnabled(true);
    }

    private ArrayList<Document> filterDocuments(final String[] type, List<Document> documents) {
        final Predicate<Document> docType = new Predicate<Document>() {
            public boolean apply(Document document) {
                return document.isThisType(type);
            }
        };

        return new ArrayList<>(Utils.filter(new HashSet<>(documents), docType));
    }
}