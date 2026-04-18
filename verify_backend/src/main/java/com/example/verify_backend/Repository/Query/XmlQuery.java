package com.example.verify_backend.Repository.Query;

import com.example.verify_backend.UtilService.FileReaderService;

public enum XmlQuery implements Query{
    ADD_NEW_XML_LIST("/sql/add_new_xml_list.sql"),
    ADD_NEW_VERSION_XML("/sql/add_new_version_xml.sql"),
    CHANGE_XML_STATUS("/sql/change_xml_status.sql"),
    GET_XML_LIGHT_INFO_LIST("/sql/get_xml_light_info_list.sql"),
    GET_LAST_XML_LIGHT_INFO("/sql/get_last_xml_light_info.sql"),
    GET_XML_DATA_BY_XML_ID("/sql/get_xml_data_by_xml_id.sql"),

    ;

    private final String query;

    XmlQuery(String path) {
        this.query = FileReaderService.readFileFromResources(path);
    }

    @Override
    public String getQuery() {
        return query;
    }

}
