package com.qingfeng.module.common.base.annotation;

import com.qingfeng.module.common.base.annotation.validator.IsMobile;
import com.qingfeng.module.common.utils.MyUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;


/**
 * @ProjectName MobileValidator
 * @author Administrator
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021/4/3 0003 19:54
 */
public class MobileValidator implements ConstraintValidator<IsMobile, String> {

    @Override
    public void initialize(IsMobile isMobile) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (StringUtils.isBlank(s)) {
                return true;
            } else {
                String regex = RegexpConstant.MOBILE_REG;
                return MyUtil.match(regex, s);
            }
        } catch (Exception e) {
            return false;
        }
    }
}