public class TaxRollParcel implements Comparable<TaxRollParcel> {

    private String section;
    private String block;
    private String parcel;
    private String name;
    private String address;
    private String mailing;
    private String acres;
    private String bookNo;
    private String pageNo;

    public TaxRollParcel(String section, String block, String parcel, String name, String address, String mailing, String acres, String bookNo, String pageNo) {
        this.section = section;
        this.block = block;
        this.parcel = parcel;
        this.name = name;
        this.address = address;
        this.mailing = mailing;
        this.acres = acres;
        this.bookNo = bookNo;
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
        this.bookNo = null;
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
        this.bookNo = null;
        this.pageNo = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getBookNo() {
        return bookNo;
    }

    public void setbookNo(String bookNo) {
        this.bookNo = bookNo;
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
        if(this.parcel != null)
            return String.format("%s-%s-%s",this.section, this.block, this.parcel);
        else
            return String.format("%s-%s",this.section, this.block);
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s|%s\n", getSecBlkPcl(), this.name, this.address, this.mailing, this.acres, this.bookNo, this.pageNo);
    }

    @Override
    public int compareTo(TaxRollParcel o) {

        try {
            if (Double.parseDouble(this.section) == Double.parseDouble(o.section)) {
                if (Integer.parseInt(this.block) == Integer.parseInt(o.block) && this.parcel != null && o.parcel != null) {
                    if(this.parcel.contains("-") && !o.parcel.contains("-")) {
                        String [] pclThisComps = this.parcel.split("-");
                        String [] pclOComps = o.parcel.split("-");
                        if(Double.parseDouble(pclThisComps[0]) == Double.parseDouble(pclOComps[0])) {
                            return Double.compare(Double.parseDouble(pclThisComps[0]), Double.parseDouble(pclOComps[0]));
                        } else {
                            return Double.compare(Double.parseDouble(pclThisComps[1]), Double.parseDouble(pclOComps[1]));
                        }
                    } else if(!this.parcel.contains("/") && !o.parcel.contains("/")) {
                        String [] pclThisComps = this.parcel.split("/");
                        String [] pclOComps = o.parcel.split("/");
                        if(Double.parseDouble(pclThisComps[0]) == Double.parseDouble(pclOComps[0])) {
                            return Double.compare(Double.parseDouble(pclThisComps[1]), Double.parseDouble(pclOComps[1]));
                        } else {
                            return Double.compare(Double.parseDouble(pclThisComps[0]), Double.parseDouble(pclOComps[0]));
                        }
                    } else {
                        return Double.compare(Double.parseDouble(this.parcel),Double.parseDouble(o.parcel));
                    }
                } else {
                    return Integer.compare(Integer.parseInt(this.block), Integer.parseInt(o.block));
                }
            } else {
                return Double.compare(Double.parseDouble(this.section), Double.parseDouble(o.section));
            }
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
