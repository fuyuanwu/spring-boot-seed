/**
 * Copyright 2015-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.weihui.finance.mapper;

import com.weihui.finance.domain.City;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @author Eddú Meléndez
 */
@Mapper
public interface CityMapper {

    // 查列表，带分页
    @Select("select * from city where state = #{state}")
    List<City> findPageByState(@Param("state") String state, RowBounds rowBounds);

    // 查列表，返回Map
    @MapKey("id")
    @Select("select * from city")
    Map<Integer, City> findPage4Map(RowBounds rowBounds);

    // 查单个
    @Select("select * from city where id = #{id}")
    City findById(@Param("id") Long id);

    // 更新
    @Update("update city set name = #{name}, state = #{state}, country = #{country} where id = #{id}")
    int update(City city);


    // 新增 方法
    @Insert("insert into city  (id, name, state, country) values  (#{id}, #{name}, #{state}, #{country})")
    @SelectKey(statement = "select SEQ_CITY_ID.nextval from dual", keyProperty = "id", before = true, resultType = Long.class)
    int insert(City city);


    // 批量插入。性能好，此方法的缺点是 不会返回插入的数目。
    @Insert("<script>" +
            "BEGIN " +
            "   <foreach collection=\"cityList\" item=\"city\" index=\"index\" separator=\";\" >\n" +
            "       insert into city (id, name, state, country) values (SEQ_CITY_ID.nextval, #{city.name}, #{city.state}, #{city.country})" +
            "   </foreach>\n" +
            ";END ;" +
            "</script>")
    int insertCities(@Param("cityList") List<City> cityList);

}
