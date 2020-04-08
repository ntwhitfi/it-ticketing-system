package com.ntwhitfi.it.ticketing.system.auth.model

import org.joda.time.DateTime

class UserRecord(var id: String?, val active: Boolean, val createdAt: DateTime, val email: String, val firstName: String,
                 val lastName: String, val notes: String?, val title: String?, val password: String) {
}