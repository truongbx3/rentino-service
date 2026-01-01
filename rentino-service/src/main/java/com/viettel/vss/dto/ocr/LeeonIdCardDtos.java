package com.viettel.vss.dto.ocr;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class LeeonIdCardDtos {

    // Mặt trước CCCD/CMND 12 số (12_id_card_front)
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IdCardFrontInfo {
        public String type;
        public String id;
        public String name;
        public String dob;
        public String gender;
        public String nationality;
        public String hometown;
        public String address;
        @JsonProperty("due_date")
        public String dueDate;

        // Thêm các field phân rã địa chỉ nếu cần
        @JsonProperty("address_town") public String addressTown;
        @JsonProperty("address_district") public String addressDistrict;
        @JsonProperty("address_ward") public String addressWard;
        @JsonProperty("address_town_code") public String addressTownCode;
        @JsonProperty("address_district_code") public String addressDistrictCode;
        @JsonProperty("address_ward_code") public String addressWardCode;

        @JsonProperty("hometown_town") public String hometownTown;
        @JsonProperty("hometown_district") public String hometownDistrict;
        @JsonProperty("hometown_ward") public String hometownWard;
        @JsonProperty("hometown_town_code") public String hometownTownCode;
        @JsonProperty("hometown_district_code") public String hometownDistrictCode;
        @JsonProperty("hometown_ward_code") public String hometownWardCode;

        public String image; // base64 ảnh crop/căn chỉnh (có thể rỗng) :contentReference[oaicite:7]{index=7}

        // Box + confidence (nếu bạn cần)
        @JsonProperty("id_box") public List<Integer> idBox;
        @JsonProperty("id_confidence") public Double idConfidence;

        @JsonProperty("name_box") public List<Integer> nameBox;
        @JsonProperty("name_confidence") public Double nameConfidence;

        @JsonProperty("dob_box") public List<Integer> dobBox;
        @JsonProperty("dob_confidence") public Double dobConfidence;

        @JsonProperty("address_box") public List<Integer> addressBox;
        @JsonProperty("address_confidence") public Double addressConfidence;

        @JsonProperty("hometown_box") public List<Integer> hometownBox;
        @JsonProperty("hometown_confidence") public Double hometownConfidence;

    }

    // Mặt sau CCCD/CMND 12 số (12_id_card_back)
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IdCardBackInfo {
        public String type;

        @JsonProperty("identification_sign")
        public String identificationSign;

        @JsonProperty("issue_date")
        public String issueDate;

        @JsonProperty("issued_at")
        public String issuedAt;

        public String image;

        @JsonProperty("identification_sign_box") public List<Integer> identificationSignBox;
        @JsonProperty("identification_sign_confidence") public Double identificationSignConfidence;

        @JsonProperty("issue_date_box") public List<Integer> issueDateBox;
        @JsonProperty("issue_date_confidence") public Double issueDateConfidence;

        @JsonProperty("issued_at_box") public List<Integer> issuedAtBox;
        @JsonProperty("issued_at_confidence") public Double issuedAtConfidence;

    }
}
