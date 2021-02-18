

package com.md.basedpc.autosize;

/**
 * ================================================
 * 屏幕适配逻辑策略类, 可通过 {@link AutoSizeConfig#init(Application, boolean, AutoAdaptStrategy)}
 * 和 {@link AutoSizeConfig#setAutoAdaptStrategy(AutoAdaptStrategy)} 切换策略
 *
 * @author wanggq
 * @date :2019-11-25 17:46
 */
//public class FragmentLifecycleCallbacksImpl extends FragmentManager.FragmentLifecycleCallbacks {
//    /**
//     * 屏幕适配逻辑策略类
//     */
//    private AutoAdaptStrategy mAutoAdaptStrategy;
//
//    public FragmentLifecycleCallbacksImpl(AutoAdaptStrategy autoAdaptStrategy) {
//        mAutoAdaptStrategy = autoAdaptStrategy;
//    }
//
//    @Override
//    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
//        if (mAutoAdaptStrategy != null) {
//            mAutoAdaptStrategy.applyAdapt(f, f.getActivity());
//        }
//    }
//
//    /**
//     * 设置屏幕适配逻辑策略类
//     *
//     * @param autoAdaptStrategy { AutoAdaptStrategy}
//     */
//    public void setAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
//        mAutoAdaptStrategy = autoAdaptStrategy;
//    }
//}
