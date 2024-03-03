package com.leo.qrscanner.workers

import com.google.mlkit.vision.barcode.common.Barcode


class ShowDataFormat {

    fun styledString(barcode: Barcode): ArrayList<String> {
        val ss: ArrayList<String> = ArrayList()
        when (barcode.valueType) {
            Barcode.TYPE_EMAIL -> {
                ss += "**E-Mail Information:**"
                ss += "Type: " + (barcode.email?.type ?: "Email") + ""
                ss += "Address: " + (barcode.email?.address) + ""
                ss += "Subject: " + (barcode.email?.subject ?: "Empty Subject") + ""
                ss += "Body: " + (barcode.email?.body ?: "Empty body") + ""
                ss += "Send Email"
            }

            Barcode.TYPE_PHONE -> {
                ss += "**Phone Number :**"
                val phone = barcode.phone
                ss += "Number: " + (phone?.number ?: "Empty") + ""
                ss += "Type: " + (phone?.type ?: "Empty") + ""
                ss += "Call"
            }

            Barcode.TYPE_WIFI -> {
                ss += "**Wi-Fi Information:**"
                val wifi = barcode.wifi
                ss += "SSID: " + (wifi?.ssid ?: "Empty") + ""
                ss += "Password: " + (wifi?.password ?: "Empty") + ""
                ss += "Encryption Type: " + (wifi?.encryptionType ?: " ")
                ss += "Setup Wifi"
            }

            Barcode.TYPE_URL -> {
                ss += ("**URL:**")
                ss += ("Link: " + (barcode.url?.url ?: ""))
                ss += "Open in Browser"
            }

            Barcode.TYPE_CONTACT_INFO -> {
                ss += "**Contact Information:**"
                val contactInfo = barcode.contactInfo

                ss += "Name: " + (contactInfo?.name ?: " ") + ""
                ss += "Email: " + (contactInfo?.emails ?: " ") + ""
                ss += "Phone: " + (contactInfo?.phones ?: " ") + ""
                ss += "Address: " + (contactInfo?.addresses ?: " ")
                ss += "Organization: " + (contactInfo?.organization ?: " ")
                ss += "Title: " + (contactInfo?.title ?: " ")
                ss += "Urls: " + (contactInfo?.urls ?: " ")
                ss += "Save contact"
            }

            Barcode.TYPE_CALENDAR_EVENT -> {
                ss += "**Calendar Event:**"
                val calEve = barcode.calendarEvent

                ss += "Start time : " + (calEve?.start ?: " ") + ""
                ss += "End Time: " + (calEve?.end ?: " ") + ""
                ss += "Location : " + (calEve?.location ?: " ") + ""
                ss += "Status : " + (calEve?.status ?: " ") + ""
                ss += "Description: " + (calEve?.description ?: " ") + ""
                ss += "Organized By: " + (calEve?.organizer ?: " ") + ""
                ss += "Summary : " + (calEve?.summary ?: " ") + ""
                ss += "Add Event"

            }

            Barcode.TYPE_GEO -> {
                ss += "** Location: **"
                val geoPoint = barcode.geoPoint
                ss += "Latitude: " + (geoPoint?.lat ?: " ") + ""
                ss += "Longitude: " + (geoPoint?.lng ?: " ")
                ss += "Open Map"
            }

            Barcode.TYPE_DRIVER_LICENSE -> {
                ss += "**Driver License:**"
                val driverLicense = barcode.driverLicense

                ss += "Name: " + (driverLicense?.firstName ?: " ") +
                        " " + (driverLicense?.middleName ?: " ") +
                        " " + (driverLicense?.lastName ?: " ") + ""
                ss += "Address: " + (driverLicense?.addressStreet ?: " ") + " " +
                        (driverLicense?.addressCity ?: " ") + " " +
                        (driverLicense?.addressState ?: " ") + " " +
                        (driverLicense?.addressZip ?: " ") + ""
                ss += "License Number: " + (driverLicense?.licenseNumber ?: " ") + ""
                ss += "Issued Date: " + (driverLicense?.issueDate ?: " ") + ""
                ss += "Expiration Date: " + (driverLicense?.expiryDate ?: " ")

                ss += "Take ScreenShot"

            }

            Barcode.TYPE_SMS -> {
                ss += "**SMS Message:**"
                val sms = barcode.sms

                ss += "Message: " + (sms?.message ?: " ") + ""
                ss += "Phone Number: " + (sms?.phoneNumber ?: " ")

                ss += "Call phone"

            }

            Barcode.TYPE_ISBN -> {
                ss += "**ISBN:**\n"
                ss += barcode.rawValue.toString()
                ss += "Open in browser"
            }

            Barcode.TYPE_PRODUCT -> {
                ss += "**Product Information:**\n"
                ss += barcode.rawValue.toString()
                ss += "Open in browser"
            }

            Barcode.TYPE_TEXT -> {
                ss += "**Plain Text:**\n"
                ss += barcode.rawValue.toString()
                ss += "Copy"
            }

            else -> {
                ss += "**Unknown Barcode :**\n"
                ss += barcode.rawValue.toString()
                ss += "Rate us"
            }
        }
        return ss
    }

    fun styledRawString(barcode: Barcode): ArrayList<String> {
        var ss: ArrayList<String> = ArrayList()
        when (barcode.valueType) {
            Barcode.TYPE_EMAIL -> {

                ss.add("**E-Mail Information:**")
                ss.add((barcode.email?.type ?: "").toString())
                ss.add(barcode.email?.address ?: "")
                ss.add(barcode.email?.subject ?: "")
                ss.add(barcode.email?.body ?: "")
            }

            Barcode.TYPE_PHONE -> {
                ss.add("**Phone Number :**")
                val phone = barcode.phone
                ss.add((phone?.number ?: "Empty"))
                ss.add(((phone?.type ?: "Empty").toString()))
            }

            Barcode.TYPE_WIFI -> {
                ss.add("**Wi-Fi Information:**")
                val wifi = barcode.wifi
                ss.add((wifi?.ssid ?: ""))
                ss.add((wifi?.password ?: ""))
                ss.add(((wifi?.encryptionType ?: " ").toString()))
            }

            Barcode.TYPE_URL -> {
                ss.add("**URL:**")
                ss.add(barcode.url.toString())
            }

            Barcode.TYPE_CONTACT_INFO -> {
                ss.add("**Contact Information:**")
                val contactInfo = barcode.contactInfo

                ss.add(((contactInfo?.name ?: " ").toString()))
                ss.add(((contactInfo?.emails ?: " ").toString()))
                ss.add(((contactInfo?.phones ?: " ").toString()))
                ss.add(((contactInfo?.addresses ?: " ").toString()))
                ss.add((contactInfo?.organization ?: " "))
                ss.add((contactInfo?.title ?: " "))
                ss.add(((contactInfo?.urls ?: "").toString()))
            }

            Barcode.TYPE_CALENDAR_EVENT -> {
                ss.add("**Calendar Event:**")
                val calEve = barcode.calendarEvent

                ss.add(((calEve?.start ?: " ").toString()))
                ss.add(((calEve?.end ?: " ").toString()))
                ss.add((calEve?.location ?: " "))
                ss.add((calEve?.status ?: " "))
                ss.add((calEve?.description ?: " "))
                ss.add((calEve?.organizer ?: " "))
                ss.add((calEve?.summary ?: " "))

            }

            Barcode.TYPE_GEO -> {
                ss.add("**Geo Point:**")
                val geoPoint = barcode.geoPoint
                ss.add(((geoPoint?.lat ?: " ").toString()))
                ss.add(((geoPoint?.lng ?: " ").toString()))
            }

            Barcode.TYPE_DRIVER_LICENSE -> {
                ss.add("**Driver License:**")
                val driverLicense = barcode.driverLicense

                ss.add(
                    (driverLicense?.firstName ?: "") +
                            " " + (driverLicense?.middleName ?: "") +
                            " " + (driverLicense?.lastName ?: "")
                )
                ss.add(
                    (driverLicense?.addressStreet ?: " ") + " " +
                            (driverLicense?.addressCity ?: " ") + " " +
                            (driverLicense?.addressState ?: " ") + " " +
                            (driverLicense?.addressZip ?: " ")
                )
                ss.add((driverLicense?.licenseNumber ?: " "))
                ss.add((driverLicense?.issueDate ?: " "))
                ss.add((driverLicense?.expiryDate ?: " "))

            }

            Barcode.TYPE_SMS -> {
                ss.add("**SMS Message:**")
                val sms = barcode.sms

                ss.add((sms?.message ?: " "))
                ss.add((sms?.phoneNumber ?: " "))

            }

            Barcode.TYPE_ISBN -> {
                ss.add("**ISBN:**")
                ss.add(barcode.rawValue.toString())
            }

            Barcode.TYPE_PRODUCT -> {
                ss.add("**Product Information:**")
                ss.add(barcode.rawValue.toString())
            }

            Barcode.TYPE_TEXT -> {
                ss.add("**Plain Text:**")
                ss.add(barcode.rawValue.toString())
            }

            else -> {
                ss.add("**Unknown Barcode :**")
                ss.add(barcode.rawValue.toString())
            }
        }
        return ss
    }

}

