/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shpp.eorlov.assignment1.di


import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.ui.auth.AuthFragment
import com.shpp.eorlov.assignment1.ui.details.DetailViewFragment
import com.shpp.eorlov.assignment1.ui.dialogfragment.ContactDialogFragment
import com.shpp.eorlov.assignment1.ui.editprofile.EditProfileFragment
import com.shpp.eorlov.assignment1.ui.signin.SignInFragment
import com.shpp.eorlov.assignment1.ui.mycontacts.MyContactsFragment
import com.shpp.eorlov.assignment1.ui.myprofile.MyProfileFragment
import dagger.Subcomponent

// Scope annotation that the ContactComponent uses
// Classes annotated with @ContactScope will have a unique instance in this Component
@ContactScope
// Definition of a Dagger subcomponent
@Subcomponent
interface ContactComponent {

    // Factory to create instances of RegistrationComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): ContactComponent
    }

    // Classes that can be injected by this Component
    fun inject(fragment: MyContactsFragment)
    fun inject(fragment: DetailViewFragment)
    fun inject(dialogFragment: ContactDialogFragment)
    fun inject(activity: MainActivity)
    fun inject(fragment: MyProfileFragment)
    fun inject(fragment: EditProfileFragment)
    fun inject(fragment: AuthFragment)
    fun inject(fragment: SignInFragment)
}
