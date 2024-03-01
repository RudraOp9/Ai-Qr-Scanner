package com.leo.qrscanner.workers

import com.google.mlkit.vision.barcode.common.Barcode


class ShowDataFormat() {

    fun styledString(barcode: Barcode): String {
        var ss: String = ""
        when (barcode.valueType) {
            Barcode.TYPE_EMAIL -> {
                ss = "**E-Mail Information:**\n"
                ss += "Type: " + (barcode.email?.type ?: "Email") + "\n"
                ss += "Address: " + (barcode.email?.address) + "\n"
                ss += "Subject: " + (barcode.email?.subject ?: "Empty Subject") + "\n"
                ss += "Body: " + (barcode.email?.body ?: "Empty body") + "\n"
            }

            Barcode.TYPE_PHONE -> {
                ss = "**Phone Number :**\n"
                val phone = barcode.phone
                ss += "Number: " + (phone?.number ?: "Empty") + "\n"
                ss += "Type: " + (phone?.type ?: "Empty") + "\n"
            }

            Barcode.TYPE_WIFI -> {
                ss = "**Wi-Fi Information:**\n"
                val wifi = barcode.wifi
                ss += "SSID: " + (wifi?.ssid ?: "Empty") + "\n"
                ss += "Password: " + (wifi?.password ?: "Empty") + "\n"
                ss += "Encryption Type: " + (wifi?.encryptionType ?: " ")
            }

            Barcode.TYPE_URL -> {
                ss = "**URL:**\n"
                ss += "Link: " + barcode.url
            }

            Barcode.TYPE_CONTACT_INFO -> {
                ss = "**Contact Information:**\n"
                val contactInfo = barcode.contactInfo

                ss += "Name: " + (contactInfo?.name ?: " ") + "\n"
                ss += "Email: " + (contactInfo?.emails ?: " ") + "\n"
                ss += "Phone: " + (contactInfo?.phones ?: " ") + "\n"
                ss += "Address: " + (contactInfo?.addresses ?: " ")
                ss += "Organization: " + (contactInfo?.organization ?: " ")
                ss += "Title: " + (contactInfo?.title ?: " ")
                ss += "Urls: " + (contactInfo?.urls ?: " ")
            }

            Barcode.TYPE_CALENDAR_EVENT -> {
                ss = "**Calendar Event:**\n"
                val calEve = barcode.calendarEvent

                ss += "Start time : " + (calEve?.start ?: " ") + "\n"
                ss += "End Time: " + (calEve?.end ?: " ") + "\n"
                ss += "Location : " + (calEve?.location ?: " ") + "\n"
                ss += "Status : " + (calEve?.status ?: " ") + "\n"
                ss += "Description: " + (calEve?.description ?: " ") + "\n"
                ss += "Organized By: " + (calEve?.organizer ?: " ") + "\n"
                ss += "Summary : " + (calEve?.summary ?: " ") + "\n"

            }

            Barcode.TYPE_GEO -> {
                ss = "**Geo Point:**\n"
                val geoPoint = barcode.geoPoint
                ss += "Latitude: " + (geoPoint?.lat ?: " ") + "\n"
                ss += "Longitude: " + (geoPoint?.lng ?: " ")
            }

            Barcode.TYPE_DRIVER_LICENSE -> {
                ss = "**Driver License:**\n"
                val driverLicense = barcode.driverLicense

                ss += "Name: " + (driverLicense?.firstName ?: " ") +
                        " " + (driverLicense?.middleName ?: " ") +
                        " " + (driverLicense?.lastName ?: " ") + "\n"
                ss += "Address: " + (driverLicense?.addressStreet ?: " ") + " " +
                        (driverLicense?.addressCity ?: " ") + " " +
                        (driverLicense?.addressState ?: " ") + " " +
                        (driverLicense?.addressZip ?: " ") + "\n"
                ss += "License Number: " + (driverLicense?.licenseNumber ?: " ") + "\n"
                ss += "Issued Date: " + (driverLicense?.issueDate ?: " ") + "\n"
                ss += "Expiration Date: " + (driverLicense?.expiryDate ?: " ")

            }

            Barcode.TYPE_SMS -> {
                ss = "**SMS Message:**\n"
                val sms = barcode.sms

                ss += "Message: " + (sms?.message ?: " ") + "\n"
                ss += "Phone Number: " + (sms?.phoneNumber ?: " ")

            }

            Barcode.TYPE_ISBN -> {
                ss = "**ISBN:**\n"
                ss += barcode.rawValue
            }

            Barcode.TYPE_PRODUCT -> {
                ss = "**Product Information:**\n"
                ss += barcode.rawValue
            }

            Barcode.TYPE_TEXT -> {
                ss = "**Plain Text:**\n"
                ss += barcode.rawValue
            }

            else -> {
                ss = "**Unknown Barcode :**\n";
                ss += barcode.rawValue;
            }
        }
        return ss
    }

}

