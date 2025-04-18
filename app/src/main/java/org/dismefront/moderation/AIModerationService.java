package org.dismefront.moderation;

import org.dismefront.publicatoin.Publication;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
public class AIModerationService {

    public Boolean moderatePublication(Publication publication) {
        return new Random().nextBoolean();
    }

}