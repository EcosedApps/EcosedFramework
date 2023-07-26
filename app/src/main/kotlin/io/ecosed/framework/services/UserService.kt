package io.ecosed.framework.services

import io.ecosed.framework.IUserService
import kotlin.system.exitProcess

class UserService : IUserService.Stub() {

    override fun destroy() {
        exitProcess(0)
    }

    override fun exit() {
        exitProcess(0)
    }
}