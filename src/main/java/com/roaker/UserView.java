package com.roaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roaker
 * @version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserView {
    private String username;
    private String msg;
}
