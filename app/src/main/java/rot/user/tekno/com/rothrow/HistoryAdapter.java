package rot.user.tekno.com.rothrow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import rot.user.tekno.com.rothrow.model.ListPengambil;

/**
 * Created by Galih on 1/5/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private List<ListPengambil> historyModel = new ArrayList<>();

    public HistoryAdapter(Context context, List<ListPengambil> historyModel) {
        this.context = context;
        this.historyModel = historyModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_history, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ListPengambil data = historyModel.get(position);
        holder.hargaSampah.setText(String.valueOf(data.getHarga()));
        holder.jenisSampah.setText(data.getJenisSp());
        holder.namaPembuang.setText(data.getNama());
        holder.statusSampah.setText(data.getStatus());
        Glide.with(context)
                .load(data.getUrlGambar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate().into(holder.fotoPembuang);
    }

    @Override
    public int getItemCount() {
        return historyModel.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaPembuang;
        TextView jenisSampah;
        TextView hargaSampah;
        TextView statusSampah;
        ImageView fotoPembuang;
        public ViewHolder(View itemView) {
            super(itemView);
                namaPembuang = (TextView) itemView.findViewById(R.id.tv_nama_siswa);
                jenisSampah = (TextView) itemView.findViewById(R.id.tv_jenissampah);
                hargaSampah = (TextView) itemView.findViewById(R.id.tv_hargasampah);
                statusSampah = (TextView) itemView.findViewById(R.id.tv_statussampah);
                fotoPembuang = (ImageView) itemView.findViewById(R.id.iv_gambar);
        }
    }

}
