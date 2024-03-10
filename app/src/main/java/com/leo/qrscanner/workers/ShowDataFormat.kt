package com.leo.qrscanner.workers

import com.google.mlkit.vision.barcode.common.Barcode
import java.util.Calendar


class ShowDataFormat {

    fun styledString(barcode: Barcode): ArrayList<String> {
        val ss: ArrayList<String> = ArrayList()
        when (barcode.valueType) {
            Barcode.TYPE_EMAIL -> {
                ss += "**E-Mail Information:** \n "
                ss += "Address: " + (barcode.email?.address) + "\n"
                ss += "Subject: " + (barcode.email?.subject ?: "Empty Subject") + "\n"
                ss += "Body: " + (barcode.email?.body ?: "Empty body") + "\n"
                ss += "Send Email"
            }

            Barcode.TYPE_PHONE -> {
                ss += "**Phone Number :**\n"
                val phone = barcode.phone
                ss += "Number: " + (phone?.number ?: "Empty") + "\n"
                ss += "Call"
            }

            Barcode.TYPE_WIFI -> {
                ss += "**Wi-Fi Information:**\n"
                val wifi = barcode.wifi
                ss += "SSID: " + (wifi?.ssid ?: "Empty") + ""
                ss += "Password: " + (wifi?.password ?: "Empty") + ""
                ss += "Encryption Type: " + (wifi?.encryptionType ?: " ")
                ss += "Setup Wifi"
            }

            Barcode.TYPE_URL -> {
                ss += ("**URL:**\n")
                ss += ("Link: " + (barcode.url?.url ?: ""))
                ss += "Open in Browser"
            }

            Barcode.TYPE_CONTACT_INFO -> {
                ss += "**Contact Information:**"

                val contactInfo = barcode.contactInfo
                if (contactInfo != null) {

                    var extra: String = ""
                    ss += "Name: " + (contactInfo.name?.formattedName ?: " ") + ""

                    for (x in contactInfo.emails) extra += "${x.address} ,"
                    ss.add("Emails: $extra ")

                    extra = ""
                    for (x in contactInfo.phones) extra += "${x.number} ,"
                    ss.add("Phone: $extra")

                    extra = ""
                    for (x in contactInfo.addresses) extra += "${x.addressLines} ,"
                    ss.add("Address: $extra")

                    ss += "Organization: " + (contactInfo.organization ?: " ")
                    ss += "Title: " + (contactInfo.title ?: " ")

                    extra = ""
                    for (x in contactInfo.urls) extra += "${x} ,"
                    ss.add(extra)

                    ss += "Save contact"

                }
            }

            Barcode.TYPE_CALENDAR_EVENT -> {
                ss += "**Calendar Event:**"
                val calEve = barcode.calendarEvent


                val beginTime = Calendar.getInstance()
                val endTime = Calendar.getInstance()
                if (calEve != null) {

                    val calIns = Calendar.getInstance()

                    beginTime.set(
                        calEve.start?.year ?: calIns.get(Calendar.YEAR),
                        calEve.start?.month ?: calIns.get(Calendar.MONTH),
                        calEve.start?.day ?: calIns.get(Calendar.DAY_OF_MONTH),
                        calEve.start?.hours ?: calIns.get(Calendar.HOUR_OF_DAY),
                        calEve.start?.minutes ?: calIns.get(Calendar.MINUTE)
                    )


                    endTime.set(
                        calEve.start?.year ?: Calendar.getInstance().get(Calendar.YEAR),
                        calEve.start?.month ?: Calendar.getInstance().get(Calendar.MONTH),
                        calEve.start?.day ?: Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    )

                    ss += "Start time : " + (beginTime.time.toString()) + ""
                    ss += "End Time: " + (endTime.time.toString()) + ""
                    ss += "Location : " + (calEve.location ?: " ") + ""
                    ss += "Status : " + (calEve.status ?: " ") + ""
                    ss += "Description: " + (calEve.description ?: " ") + ""
                    ss += "Organized By: " + (calEve.organizer ?: " ") + ""
                    ss += "Summary : " + (calEve.summary ?: " ") + ""
                    ss += "Add Event"
                }


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

                ss += "Donate"

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
                if (barcode.rawValue.toString().startsWith("upi", true)) {
                    ss += "**Plain Text:**"
                    //TODO
                }

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
                ss.add(barcode.email?.address ?: "")
                ss.add(barcode.email?.subject ?: "")
                ss.add(barcode.email?.body ?: "")
            }

            Barcode.TYPE_PHONE -> {
                ss.add("**Phone Number :**")
                val phone = barcode.phone
                ss.add((phone?.number ?: "Empty"))
            }

            Barcode.TYPE_WIFI -> {
                ss.add("**Wi-Fi Information:**")
                val wifi = barcode.wifi
                ss.add((wifi?.ssid ?: ""))
                ss.add((wifi?.password ?: ""))
                if (wifi != null) {
                    when (wifi.encryptionType) {
                        1 -> ss.add("TYPE_OPEN")
                        2 -> ss.add("TYPE_WPA")
                        3 -> ss.add("TYPE_WEP")
                        else -> ss.add("NOT_DEFINED")
                    }
                }

            }

            Barcode.TYPE_URL -> {
                ss.add("**URL:**")
                barcode.url?.url?.let { ss.add(it) }

            }

            Barcode.TYPE_CONTACT_INFO -> {
                ss.add("**Contact Information:**") //0
                val contactInfo = barcode.contactInfo
                if (contactInfo != null) {
                    var extra: String = ""
                    ss.add(((contactInfo.name?.formattedName ?: " ")))
                    for (x in contactInfo.emails) extra += "${x.address} ,"
                    ss.add(extra)
                    extra = ""
                    for (x in contactInfo.phones) extra += "${x.number} ,"
                    ss.add(extra)
                    extra = ""
                    for (x in contactInfo.addresses) extra += "${x.addressLines} ,"
                    ss.add(extra)
                    ss.add((contactInfo.organization ?: " "))
                    ss.add((contactInfo.title ?: " "))
                    extra = ""
                    for (x in contactInfo.urls) extra += "${x} ,"
                    ss.add(extra)
                }


            }

            Barcode.TYPE_CALENDAR_EVENT -> {
                ss.add("**Calendar Event:**")
                val calEve = barcode.calendarEvent
                val beginTime = Calendar.getInstance()
                val endTime = Calendar.getInstance()
                if (calEve != null) {

                    val calIns = Calendar.getInstance()

                    beginTime.set(
                        calEve.start?.year ?: calIns.get(Calendar.YEAR),
                        calEve.start?.month ?: calIns.get(Calendar.MONTH),
                        calEve.start?.day ?: calIns.get(Calendar.DAY_OF_MONTH)
                    )

                    endTime.set(
                        calEve.start?.year ?: Calendar.getInstance().get(Calendar.YEAR),
                        calEve.start?.month ?: Calendar.getInstance().get(Calendar.MONTH),
                        calEve.start?.day ?: Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    )


                    ss.add(beginTime.timeInMillis.toString())
                    ss.add(endTime.timeInMillis.toString())
                    ss.add((calEve.location ?: " "))
                    ss.add((calEve.status ?: " "))
                    ss.add((calEve.description ?: " "))
                    ss.add((calEve.organizer ?: " "))
                    ss.add((calEve.summary ?: " "))

                }
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

