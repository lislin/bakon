/*! JointJS v0.9.6 - JavaScript diagramming library  2015-12-19 


This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
//      JointJS library.
//      (c) 2011-2013 client IO

joint.shapes.feature = {};

joint.shapes.feature.E = joint.dia.Element.extend({
    markup: '<g class="rotatable"><text class="label"/><image/></g>',

    defaults: joint.util.deepSupplement({
        type: 'feature.E',
        size: { width: 50, height: 50 },
        attrs: {
            image: {
                width: 48, height: 48,
                'xlink:href': 'images/features/E.png'
            },
            '.label': {
                'font-weight': '800',
                ref: 'image', 'ref-x': 38, 'ref-y': -15,
                'font-family': 'Courier New', 'font-size': 14,
                'text-anchor': 'end',
            }
        },
        setting: { type: 'E', position: '', host: '', port: '1' }
    }, joint.dia.Element.prototype.defaults)
});

joint.shapes.feature.W = joint.dia.Element.extend({
    markup: '<g class="rotatable"><text class="label"/><image/></g>',

    defaults: joint.util.deepSupplement({
        type: 'feature.W',
        size: { width: 50, height: 50 },
        attrs: {
            image: {
                width: 48, height: 48,
                'xlink:href': 'images/features/W.png'
            },
            '.label': {
                'font-weight': '800',
                ref: 'image', 'ref-x': 38, 'ref-y': -15,
                'font-family': 'Courier New', 'font-size': 14,
                'text-anchor': 'end',
            }
        },
        setting: { type: 'W', position: '', host: '', port: '1' }
    }, joint.dia.Element.prototype.defaults)
});

joint.shapes.feature.T = joint.dia.Element.extend({
    markup: '<g class="rotatable"><text class="label"/><image/></g>',

    defaults: joint.util.deepSupplement({
        type: 'feature.T',
        size: { width: 50, height: 50 },
        attrs: {
            image: {
                width: 48, height: 48,
                'xlink:href': 'images/features/T.png'
            },
            '.label': {
                'font-weight': '800',
                ref: 'image', 'ref-x': 38, 'ref-y': -15,
                'font-family': 'Courier New', 'font-size': 14,
                'text-anchor': 'end',
            }
        },
        setting: { type: 'T', position: '', host: '', port: '1' }
    }, joint.dia.Element.prototype.defaults)
});
