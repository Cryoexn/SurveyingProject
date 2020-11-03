public class TaxRollParcel {

    private String section;
    private String block;
    private String parcel;
    private String name;
    private String address;
    private String mailing;
    private String acres;
    private String instrument;
    private String instrumentNo;
    private String pageNo;

    public TaxRollParcel(String section, String block, String parcel, String name, String address, String mailing, String acres, String instrument, String instrumentNo, String pageNo) {
        this.section = section;
        this.block = block;
        this.parcel = parcel;
        this.name = name;
        this.address = address;
        this.mailing = mailing;
        this.acres = acres;
        this.instrument = instrument;
        this.instrumentNo = instrumentNo;
        this.pageNo = pageNo;
    }

    public TaxRollParcel(String section, String block, String parcel) {
        this.section = section;
        this.block = block;
        this.parcel = parcel;
        this.name = null;
        this.address = null;
        this.mailing = null;
        this.acres = null;
        this.instrument = null;
        this.instrumentNo = null;
        this.pageNo = null;
    }

    public TaxRollParcel() {
        this.section = null;
        this.block = null;
        this.parcel = null;
        this.name = null;
        this.address = null;
        this.mailing = null;
        this.acres = null;
        this.instrument = null;
        this.instrumentNo = null;
        this.pageNo = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailing() {
        return mailing;
    }

    public void setMailing(String mailing) {
        this.mailing = mailing;
    }

    public String getAcres() {
        return acres;
    }

    public void setAcres(String acres) {
        this.acres = acres;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getInstrumentNo() {
        return instrumentNo;
    }

    public void setInstrumentNo(String instrumentNo) {
        this.instrumentNo = instrumentNo;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getParcel() {
        return parcel;
    }

    public void setParcel(String parcel) {
        this.parcel = parcel;
    }

    public void setParcel(String[] parcel) {
        this.parcel = parcel[0] + parcel[1];
    }

    public String getSecBlkPcl() {
        return String.format("%s-%s-%s",this.section, this.block, this.parcel);
    }

    @Override
    public String toString() {
        return "TaxRollParcel{" +
                "section='" + section + '\'' +
                ", block='" + block + '\'' +
                ", parcel='" + parcel + '\'' +
                ", name='" + name + '\'' +
                ", mailing='" + mailing + '\'' +
                ", acres='" + acres + '\'' +
                ", instrument='" + instrument + '\'' +
                ", instrumentNo='" + instrumentNo + '\'' +
                ", pageNo='" + pageNo + '\'' +
                '}';
    }
}
