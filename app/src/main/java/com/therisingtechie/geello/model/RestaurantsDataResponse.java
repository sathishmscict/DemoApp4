package com.therisingtechie.geello.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SATHISH on 07-Oct-17.
 */

public class RestaurantsDataResponse {



        @SerializedName("flag")
        @Expose
        private Boolean flag;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

        public Boolean getFlag() {
            return flag;
        }

        public void setFlag(Boolean flag) {
            this.flag = flag;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }




    public class Address {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("restro_id")
        @Expose
        private String restroId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("street_1")
        @Expose
        private String street1;
        @SerializedName("street_2")
        @Expose
        private String street2;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("locality")
        @Expose
        private String locality;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("created_on")
        @Expose
        private Integer createdOn;
        @SerializedName("updated_on")
        @Expose
        private Integer updatedOn;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRestroId() {
            return restroId;
        }

        public void setRestroId(String restroId) {
            this.restroId = restroId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getStreet1() {
            return street1;
        }

        public void setStreet1(String street1) {
            this.street1 = street1;
        }

        public String getStreet2() {
            return street2;
        }

        public void setStreet2(String street2) {
            this.street2 = street2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLocality() {
            return locality;
        }

        public void setLocality(String locality) {
            this.locality = locality;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public Integer getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(Integer createdOn) {
            this.createdOn = createdOn;
        }

        public Integer getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(Integer updatedOn) {
            this.updatedOn = updatedOn;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }


    public class Datum {

        public Datum(String id, String name, String description, String createdAt, String image, String imageId, String logo, String logoId, Boolean isActive, String updatedAt, String email, String mobile, Object restroTimings, List<Address> addresses, List<Object> products, String isPopular) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.createdAt = createdAt;
            this.image = image;
            this.imageId = imageId;
            this.logo = logo;
            this.logoId = logoId;
            this.isActive = isActive;
            this.updatedAt = updatedAt;
            this.email = email;
            this.mobile = mobile;
            this.restroTimings = restroTimings;
            this.addresses = addresses;
            this.products = products;
            this.isPopular = isPopular;
        }

       /* public List<RestaurantsDataResponse.Datum> getGenreFilteredMovies(List<String> genre, List<RestaurantsDataResponse.Datum> mList) {
            List<RestaurantsDataResponse.Datum> tempList = new ArrayList<>();
            for (RestaurantsDataResponse.Datum restaurant : mList) {
                for (String g : genre) {
                    if (restaurant.get.getGenre().equalsIgnoreCase(g)) {
                        tempList.add(movie);
                    }
                }

            }
            return tempList;
        }*/

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("image_id")
        @Expose
        private String imageId;
        @SerializedName("logo")
        @Expose
        private String logo;
        @SerializedName("logo_id")
        @Expose
        private String logoId;
        @SerializedName("is_active")
        @Expose
        private Boolean isActive;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("restro_timings")
        @Expose
        private Object restroTimings;
        @SerializedName("addresses")
        @Expose
        private List<Address> addresses = null;
        @SerializedName("products")
        @Expose
        private List<Object> products = null;
        @SerializedName("is_popular")
        @Expose
        private String isPopular;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getLogoId() {
            return logoId;
        }

        public void setLogoId(String logoId) {
            this.logoId = logoId;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Object getRestroTimings() {
            return restroTimings;
        }

        public void setRestroTimings(Object restroTimings) {
            this.restroTimings = restroTimings;
        }

        public List<Address> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<Address> addresses) {
            this.addresses = addresses;
        }

        public List<Object> getProducts() {
            return products;
        }

        public void setProducts(List<Object> products) {
            this.products = products;
        }

        public String getIsPopular() {
            return isPopular;
        }

        public void setIsPopular(String isPopular) {
            this.isPopular = isPopular;
        }

    }



}
