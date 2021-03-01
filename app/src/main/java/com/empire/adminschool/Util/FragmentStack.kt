package com.empire.adminschool.Util


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentStack {

    companion object{

        fun replaceFragmentToContainer(container: Int, fragmentManager: FragmentManager, fragment: Fragment, tag: String){
            var transaction = fragmentManager.beginTransaction()
            transaction.replace(container, fragment, tag)
//            transaction.setCustomAnimations(R.anim.sliding_in_left, R.anim.sliding_out_right)
            //onBackPressed work If uncomment.
            transaction.addToBackStack(tag)
            transaction.commit()
        }

        fun addFragmentToContainer(container: Int, fragmentManager: FragmentManager, fragment: Fragment, tag: String){
            var transaction = fragmentManager.beginTransaction()
            if (fragmentManager.findFragmentByTag(tag) == null) { // No fragment in backStack with same tag..
                transaction.add(container, fragment, tag)
                transaction.addToBackStack(tag)  //onBackPressed work If uncomment.
                // transaction.setCustomAnimations(R.anim.sliding_in_left, R.anim.sliding_out_right)
                transaction.commit()
            } else {
                transaction.show(fragmentManager.findFragmentByTag(tag)!!).commit()
            }
        }

    }

}
