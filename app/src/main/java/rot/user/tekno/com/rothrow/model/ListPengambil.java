package rot.user.tekno.com.rothrow.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Galih on 12/30/2017.
 */

public class ListPengambil {
    @SerializedName("id_order")
    public int ido;
    @SerializedName("id")
    public int id;
    @SerializedName("ro_nama_pembuang")
    public String nama;
    @SerializedName("ro_jenis_sampah")
    public String jenisSp;
    @SerializedName("ro_mode_pembuangan")
    public String modePb;
    @SerializedName("ro_alamat")
    public String alamat;
    @SerializedName("ro_lat")
    public double lat;
    @SerializedName("ro_lang")
    public double lang;
    @SerializedName("ro_harga")
    public int harga;
    @SerializedName("ro_status")
    public String status;

    public ListPengambil(int ido, int id, String nama, String jenisSp, String modePb, String alamat, double lat, double lang, int harga, String status) {
        this.ido = ido;
        this.id = id;
        this.nama = nama;
        this.jenisSp = jenisSp;
        this.modePb = modePb;
        this.alamat = alamat;
        this.lat = lat;
        this.lang = lang;
        this.harga = harga;
        this.status = status;
    }

    public int getIdo() {
        return ido;
    }

    public void setIdo(int ido) {
        this.ido = ido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenisSp() {
        return jenisSp;
    }

    public void setJenisSp(String jenisSp) {
        this.jenisSp = jenisSp;
    }

    public String getModePb() {
        return modePb;
    }

    public void setModePb(String modePb) {
        this.modePb = modePb;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
