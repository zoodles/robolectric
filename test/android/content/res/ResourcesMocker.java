package android.content.res;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ResourcesMocker {
    public static Resources mockedResources() {
        Resources resources = spy(Resources.getSystem());
        doAnswer(new Answer<XmlResourceParser>() {
            @Override
            public XmlResourceParser answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] arguments = invocationOnMock.getArguments();
                System.out.println("arguments = " + arguments);
                return null;
            }
        }).when(resources).loadXmlResourceParser(any(Integer.TYPE), any(String.class));
//        when(resources.loadXmlResourceParser(any(Integer.class), any(String.class))).thenReturn(null);
        return resources;
    }
}
