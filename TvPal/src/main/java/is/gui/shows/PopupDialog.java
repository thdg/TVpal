package is.gui.shows;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.io.IOException;
import is.contracts.datacontracts.SeriesData;
import is.tvpal.R;
import is.utilities.PictureTask;

public class PopupDialog
{
    public static void ShowSeriesInfo(Context mContext, SeriesData show)
    {
        final PopupWindow mPopupWindow = new PopupWindow(mContext);

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_show, null);

        TextView showTitle = (TextView) popupView.findViewById(R.id.popupTitle);
        TextView showOverview = (TextView) popupView.findViewById(R.id.popupOverview);
        TextView showNetwork = (TextView) popupView.findViewById(R.id.popupNetwork);
        TextView showFirstAired = (TextView) popupView.findViewById(R.id.popupFirstAired);
        Button showClose = (Button) popupView.findViewById(R.id.popUpClose);
        ImageView showBanner = (ImageView) popupView.findViewById(R.id.popupBanner);

        if (show.getBanner() != null)
            new DownloadBannerBitmap(show.getBanner()).execute(showBanner);

        showTitle.setText(show.getTitle());
        String overview = show.getOverview()!=null ? show.getOverview() : "";
        showOverview.setText(overview);
        showNetwork.setText(show.getNetwork());
        showFirstAired.setText(show.getFirstAired());

        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        showClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setContentView(popupView);
        mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    private static class DownloadBannerBitmap extends AsyncTask<ImageView, Void, Bitmap>
    {
        private String apiUrl = "http://thetvdb.com/banners/";
        private String bannerUrl;

        public DownloadBannerBitmap(String bannerUrl)
        {
            this.bannerUrl = bannerUrl;
        }

        @Override
        protected Bitmap doInBackground(ImageView... views)
        {
            try
            {
                return GetBanner(views[0]);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        private Bitmap GetBanner(ImageView imageView) throws IOException
        {
            try
            {
                String url = String.format("%s%s", apiUrl, bannerUrl);

                Bitmap banner = PictureTask.getBitmapFromUrl(url);

                imageView.setImageBitmap(banner);
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return null;
        }
    }
}
