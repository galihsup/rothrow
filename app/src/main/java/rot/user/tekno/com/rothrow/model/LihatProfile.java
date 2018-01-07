package rot.user.tekno.com.rothrow.model;

/**
 * Created by Galih on 12/31/2017.
 */

public class LihatProfile {
    public String namaPengguna;
    public String alamat;
    public String email;
    public String tgl;
    public String status;

    public LihatProfile(String namaPengguna, String alamat, String email, String tgl, String status) {
        this.namaPengguna = namaPengguna;
        this.alamat = alamat;
        this.email = email;
        this.tgl = tgl;
        this.status = status;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }

    public void setNamaPengguna(String namaPengguna) {
        this.namaPengguna = namaPengguna;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
