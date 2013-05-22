package net.patchingzone.ru4real.fragments;

import java.util.ArrayList;

import net.patchingzone.ru4real.L;
import net.patchingzone.ru4real.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FragmentDistances extends Fragment {

	private static final String TAG = "Fdistance";
	View v;
	ArrayList<Sound> list = new ArrayList<Sound>();

	CustomAdapter adapter;
	ListView listview;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.fragment_distances, container, false);
		return v;

	}

	@SuppressLint("NewApi")
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listview = (ListView) v.findViewById(R.id.fragment_distances_listview);
		// String[] values = new String[] { "" };

		/*
		 * for (int i = 0; i < values.length; ++i) { list.add(values[i]); }
		 */

		adapter = new CustomAdapter(getActivity(), list);
		listview.setAdapter(adapter);
		listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
		listview.setStackFromBottom(true);
		
		/*
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);

				
				  view.animate().setDuration(2000).alpha(0).withEndAction(new
				  Runnable() {
				  
				  @Override public void run() { list.remove(item);
				  adapter.notifyDataSetChanged(); view.setAlpha(1); } });
				 
			}

		});
		*/ 
		
/*
		listview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
*/
	}


	public void addItem(final String soundName, final String distance, final String soundVolume) {

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {


			
				@Override
				public void run() {

					int position = -1;
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).name.contains(soundName)) {
							position = i;
						}
					}

					if (position == -1) {
						list.add(new Sound(soundName, distance, soundVolume));
					} else {
						//L.d("add", soundName);
						list.get(position).distance = distance;
						list.get(position).volume = soundVolume;

					}

					adapter.notifyDataSetChanged();
					listview.invalidateViews();
					// listview.scrollBy(0, 0);
				}
			});
		}

	}

	public void clear() {
		list.clear();
		adapter.notifyDataSetChanged();

	} 

	private class CustomAdapter extends BaseAdapter {

		ArrayList<Sound> list_;

		public CustomAdapter(Context context, ArrayList<Sound> list) {
			super();

			list_ = list;
			//L.d("qq", "custom adapter create");
			// for (int i = 0; i < list.size(); ++i) {
			// mIdMap.put(list.get(i), i);
			// }

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			ViewHolder holder;
			//L.d("view", "getting view");

			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.view_sounds_info, null);
				holder = new ViewHolder();
				holder.name = (TextView) v.findViewById(R.id.soundName);
				holder.volume = (TextView) v.findViewById(R.id.soundVolume);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

			Sound sound = (Sound) list_.get(position);
			if (sound != null) {
				holder.name.setText(sound.name);
				holder.volume.setText(sound.volume);
				// holder.item2.setText(custom.getSecond());
			}
			return v;
		}

		/*
		 * @Override public long getItemId(int position) { //String item =
		 * getItem(position);
		 * 
		 * return mIdMap.get(item); }
		 */

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public int getCount() {
			return list_.size();
		}

		@Override
		public Object getItem(int position) {
			return list_.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	class Sound {
		String name;
		String volume;
		String distance;

		public Sound(String soundName, String distance, String soundVolume) {
			this.name = soundName;
			this.volume = soundVolume;
			this.distance = distance;

		}
	}

	private class ViewHolder {
		TextView name;
		TextView volume;
	}
}