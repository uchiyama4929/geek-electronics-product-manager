package com.example.demo.form;

import com.example.demo.validation.ValidCategoryId;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryForm implements Serializable {
    @Size(max = 255, message = "カテゴリ名は255文字以下で入力してください。")
    private String keyword;

    @ValidCategoryId
    @Pattern(regexp = "\\d*|", message = "大カテゴリは数字である必要があります")
    private String largeCategory;

    @ValidCategoryId
    @Pattern(regexp = "\\d*|", message = "中カテゴリは数字である必要があります")
    private String middleCategory;

    @ValidCategoryId
    @Pattern(regexp = "\\d*", message = "小カテゴリは数字である必要があります")
    private String smallCategory;
}
