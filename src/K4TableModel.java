/***********************************************************************************************************************
 *
 * Author:    Charlie Skelton, Skelton Consulting GmbH
 * Copyright: Skelton Consulting GmbH, Stuttgart, Germany
 * License:   Creative Commons, Attribution-ShareAlike 2.0
 *            see http://creativecommons.org/licenses/by-sa/2.0/legalcode
 *            or the included license.txt file for full license details
 */
import java.io.UnsupportedEncodingException;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Array;
import java.util.Arrays;

import kx.c;

public class K4TableModel extends AbstractTableModel implements KTableModel
{
    private boolean [] keys=     new boolean[0];
    private Object  [] columns=  new Object[0];
    private String  [] headers=  new String [0];
    private int        rowCount= 0;
    private boolean [] charArrayColumns = new boolean[0];

    public void setData( Object obj) throws UnsupportedEncodingException
    {
        if( obj instanceof c.Dict)
        {
            // We get a dict when we have a key on a table. It is a dict with a flip as the key
            // and a flip as the value
            c.Dict d= (c.Dict)obj;

            if( (d.x instanceof c.Flip) && (d.y instanceof c.Flip))
            {
                int nkeys= ((c.Flip)d.x).x.length;

                int nCols= nkeys +((c.Flip)d.y).x.length;
                keys= new boolean[nCols];

                for( int i=0;i<nkeys;i++)
                {
                    keys[i]=true;
                }

                obj= c.td( obj);
            }
        }

        if( obj instanceof c.Flip)
        {
            c.Flip f= (c.Flip) obj;

            headers= f.x;

            if( keys.length != headers.length)
            {
                keys= new boolean[headers.length];
            }

            columns= f.y;

            if( columns.length > 0)
            {
                if( columns[0].getClass().isArray())
                {
                    rowCount= Array.getLength( columns[0]);
                }
            }

//            charArrayColumns = new boolean[columns.length];
//            for (int i = columns.length - 1; i > 0; i--)
//            {
//                if(isObjectArray(columns[i]))
//                {
//                    charArrayColumns[i] = true;
//                }
//            }
//            System.out.printf("charArrayColumns: %s\n", Arrays.toString(charArrayColumns));
        }
    }

    public static boolean isTable( Object obj)
    {
        if( obj instanceof c.Flip)
        {
            return true;
        }
        else if( obj instanceof c.Dict)
        {
            c.Dict d= (c.Dict)obj;

            if( (d.x instanceof c.Flip) && (d.y instanceof c.Flip))
            {
                return true;
            }
        }

        return false;
    }

    public K4TableModel()
    {
    }

    public K4TableModel( Object obj) throws UnsupportedEncodingException
    {
//        System.out.println("K4TableModel constructor");
        setData( obj);
    }

    public boolean isKey( int column)
    {
        return keys[column];
    }

    public int getColumnCount()
    {
        return columns.length;
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public Object getValueAt(int row, int col)
    {
//        System.out.printf("K4TableModel> getValueAt row:%d col:%d\n", row, col);

        Object o = c.at(columns[ col],row);

            if(isObjectArray(o) && isCharArray(o))
            {
                o = "\"" + String.valueOf((char[]) o) + "\"";
            }


        return o;
    }


    public String getColumnName( int i)
    {
        return headers[ i];
    }

    public Class getColumnClass(int col)
    {
        return columns[col].getClass().getComponentType();
    }

    private boolean isObjectArray(Object o)
    {
        return o.getClass().isArray();
    }
    private boolean isCharArray(Object o)
    {
        return "char".equals((o.getClass().getComponentType()).getName());
    }

//    private boolean isCharArray(Object o)
//    {
//        Object[] oa = null;
//        oa.getClass();
//
//        if (o.getClass().isArray() &&  (((Object[])o).length > 0) )
//        {
//
//        }
//    }
};

