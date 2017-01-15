package co.nz.psyqor.androidthings.futureframes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import static android.R.attr.path;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { ScreenSlidePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScreenSlidePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScreenSlidePageFragment extends Fragment {
    private static final String TAG = ScreenSlidePageFragment.class.getSimpleName();

    private static final String ARG_ITEM_NUMBER = "arg_item_number";

    private int mItemNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.slide_image);
        setImage(imageView, mItemNumber);
//        Drawable myDrawable = getResources().getDrawable(R.drawable.img_2016_0);
//        imageView.setImageDrawable(myDrawable);
//        imageView.setImageDrawable(R.drawable.img_2016_0);
        return rootView;
    }

    // Sets the image at position to the given imageView
    public void setImage(ImageView imageView, int position) {
        String path = "img_2016_" + position;
        Log.d(TAG, "Loading image at path: "+ path + " from package " + MyApp.getContext().getPackageName());

//            R.drawable.img_2016_0
        int imageResource = getResources().getIdentifier(path, "drawable", MyApp.getContext().getPackageName());
        Log.d(TAG, "loading image with resource: "+imageResource);
        imageView.setImageResource(imageResource);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResource);
//        imageView.setImageBitmap(bitmap);
    }

    public ScreenSlidePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itemNumber Parameter 1.
     * @return A new instance of fragment ScreenSlidePageFragment.
     */
    public static ScreenSlidePageFragment newInstance(int itemNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_NUMBER, itemNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemNumber = getArguments().getInt(ARG_ITEM_NUMBER);
        }
    }

//    private OnFragmentInteractionListener mListener;
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
